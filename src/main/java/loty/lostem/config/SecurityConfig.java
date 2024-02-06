package loty.lostem.config;

import lombok.RequiredArgsConstructor;
import loty.lostem.jwt.*;
import loty.lostem.security.UserRole;
import loty.lostem.service.CustomUserDetailsService;
import loty.lostem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity   // 기본적인 web 보완 활성화(스프링 시큐리티 필터(SecurityConfig)가 스프링 체인필터에 등록됨 >> 시큐리티 컴퓨터에 내가 이제부터 등록할 필터가 기본 필터체인에 통보됨)
//@EnableMethodSecurity   // @EnableGlobalMethodSecurity(prePostEnabled = true)도 deprecated 되므로 해결용으로 사용. EnableGlobal~~ 이 secured 어노테이션 활성화(컨트롤러에 @Secured("ROLE_USER")로 간단하게 사용, prePostEnabled는 @prePostEnabled 어노테이션 활성화("hasRole('USER')"), postAuthorize도 같이 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final CorsConfig corsConfig;
    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired  // 시큐리티가 로그인 과정에서 password 가로챌 때 어떤 해쉬로 암호화했는지 확인
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                //.headers(http.headers().frameOptions().sameOrigin())
                //.csrf(csrf -> csrf.disable())
                .addFilterBefore(corsConfig.corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                "/api/users/signup", "/api/users/pw", "/api/users/find", "/api/users/check", "/api/users/read",
                                "/api/lost/read/**", "/api/lost/search", "/api/found/read/**", "/api/found/search",
                                "/api/appraisals/read",
                                "/api/login", "/api/logout", "/api/access", "/api/refresh",
                                "/api/websocket/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/admin/**"
                        )
                        .hasRole(UserRole.ADMIN.name())

                        .requestMatchers(
                                "/api/posts", "/api/users", "/api/lost", "/api/found",
                                "/api/appraisals", "/api/reports", "/api/keyword"
                        )
                        .hasAnyRole(UserRole.ADMIN.name(), UserRole.USER.name())
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.disable());

                return http.build();
    }
}
