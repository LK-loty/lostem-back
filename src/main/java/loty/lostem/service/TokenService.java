package loty.lostem.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.UserDTO;
import loty.lostem.entity.RefreshToken;
import loty.lostem.entity.User;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.repository.RefreshTokenRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String createAccessToken(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for provided data"));

        return tokenProvider.createToken(user);
    }

    public String createRefreshToken(UserDTO userDTO) {
        log.info("리프레시 토큰 가져오기");
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(userDTO.getUserId()).orElse(null);

        if (refreshTokenEntity == null) {
            log.info("처음 로그인 시 토큰 발급합니다");
            String newRefreshToken = tokenProvider.createRefreshToken(userDTO.getUsername());
            refreshTokenEntity = new RefreshToken(userDTO.getUserId(), newRefreshToken);
            refreshTokenRepository.save(refreshTokenEntity);
            return newRefreshToken;
        } else {
            log.info("처음이 아닌 경우 리프레시 토큰을 확인합니다");
            String refreshToken = refreshTokenEntity.getRefreshToken();
            if (tokenProvider.validateToken(refreshToken)) {
                return refreshToken;
            } else {
                log.info("리프레시 토큰이 만료되어 업데이트 합니다");
                String newRefreshToken = tokenProvider.createRefreshToken(userDTO.getUsername());
                refreshTokenEntity.update(newRefreshToken);
                refreshTokenRepository.save(refreshTokenEntity);
                return newRefreshToken;
            }
        }
    }

    public String createNewAccessToken(String refreshToken) { // refresh 토큰 검증하고 access 토큰 생성
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        // 리프레시 토큰으로 사용자 정보 가져오기
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token not found"));

        Long userId = findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));

        return tokenProvider.createToken(user);
    }

    public Long getUserId(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                userId = tokenProvider.getUserId(token);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return userId;
    }

    public boolean checkToken(String token) {
        if (tokenProvider.validateToken(token)) {
            return true;
        }
        return false;
    }

    // refresh 토큰으로 refresh 객체 검색해서 반환
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
    }

    public boolean deleteRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (refreshTokenOptional.isPresent()) {
            refreshTokenRepository.delete(refreshTokenOptional.get());
            return true;
        }
        return false;
    }
}
