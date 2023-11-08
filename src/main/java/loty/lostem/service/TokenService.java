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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /*public String authorize(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        // authenticationToken 이용해서 authentication 객체 생성 후 authenticate 메소드 실행될 때 loadUserByUsername 메소드 실행. 이 객체를 securityContext에 저장 후 인증 객체를 통해 토큰 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        // 토큰을 헤더와 dto를 통해 바디에도 넣어 전송
        //HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.TOKEN_PREFIX + jwt);

        return jwt;
    }*/

    public String createAccessToken(UserDTO userDTO) {
        // 로그인 바로 되는 것이 아니니까
        /*UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // +++
        // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
	// 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.
        */

        User user = userRepository.findById(userDTO.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("No data found for provided data"));

        return tokenProvider.createToken(user);
    }

    public String createRefreshToken(UserDTO userDTO) {
        String token = tokenProvider.createRefreshToken(userDTO.getUsername());

        RefreshToken refreshToken = new RefreshToken(userDTO.getUser_id(), token);
        refreshTokenRepository.save(refreshToken);
        return token;
    }


    public String createNewAccessToken(String refreshToken) { // refresh 토큰 검증하고 access 토큰 생성
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = findByRefreshToken(refreshToken).getUser_id();
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
