package loty.lostem.service;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.LoginDTO;
import loty.lostem.dto.TokenDTO;
import loty.lostem.dto.UserDTO;
import loty.lostem.entity.RefreshToken;
import loty.lostem.entity.User;
import loty.lostem.jwt.JwtFilter;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.repository.RefreshTokenRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

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
        // 사용자의 리프레시 토큰 가져오기
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(userDTO.getUserId()).orElse(null);

        if (refreshTokenEntity == null) {
            // 처음 로그인하는 경우
            String newRefreshToken = tokenProvider.createRefreshToken(userDTO.getUsername());
            refreshTokenEntity = new RefreshToken(userDTO.getUserId(), newRefreshToken);
            refreshTokenRepository.save(refreshTokenEntity);
            return newRefreshToken;
        } else {
            String refreshToken = refreshTokenEntity.getRefreshToken();
            if (tokenProvider.validateToken(refreshToken)) {
                return refreshToken;
            } else {
                // 리프레시 토큰이 만료된 경우 새로 발급하여 업데이트
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

    // refresh 토큰으로 refresh 객체 검색해서 반환
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected Token"));
    }
}
