/*
package loty.lostem.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.LoginDTO;
import loty.lostem.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;

import static org.springframework.security.config.Elements.JWT;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // JwtAuthenticationFilter : 진입
        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        LoginDTO loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        // authenticate() 호출되면 인증 프로바이더가 유저 디테일 서비스의 loadUserByUsername(토큰의 첫번째 파라메터) 호출
        // UserDetails를 리턴받아 토큰의 두번째 파라메터(credential)과 UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴

        // 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입, 암호화 방식은 BCryptPasswordEncoder >> 인증 프로바이더에게 알려줄 필요 없음
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return authentication;
    }

    // JWT Token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(customUserDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id", customUserDetails.getUsername())
                .withClaim("username", customUserDetails.getPassword())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
    }

}
*/
