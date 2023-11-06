package loty.lostem.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override // 실제 필터링 로직, jwt의 인증 정보를 현재 실행 중인 securityContext 저장하는 역할. 요청이 들어오면 authentication 객체 저장해서 사용
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest); // request 에서 토큰을 받고
        String requestURI = httpServletRequest.getRequestURI();

        // 가져온 토큰 값이 유효한지 확인, 유효하면 인증정보 설정
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 토큰의 유효성 검사 로직 통과
            Authentication authentication = tokenProvider.getAuthentication(jwt); // 토큰이 정상적이라면 토큰에서 authentication 객체를 받아와서
            SecurityContextHolder.getContext().setAuthentication(authentication); // securityContext 에 set
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // 필터링을 위해 토큰 정보가 필요하므로 Request Header 에서 토큰 정보를 가져옴(요청 헤더의 authorization 키 값 조회 후 가져온 값에서 접두사 제거)
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
