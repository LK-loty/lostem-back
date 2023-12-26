package loty.lostem.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.LoginDTO;
import loty.lostem.dto.UserDTO;
import loty.lostem.service.TokenService;
import loty.lostem.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginDTO loginDTO) {
        UserDTO userDTO = userService.loginUser(loginDTO);

        String accessToken = tokenService.createAccessToken(userDTO);
        String refreshToken = tokenService.createRefreshToken(userDTO);

        // AccessToken 응답 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);

        // RefreshToken 쿠키에 담기
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; HttpOnly");

        if (accessToken != null && refreshToken != null) {
            return ResponseEntity.ok()
                    .headers(headers)
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        // 쿠키, db 에서 refresh 삭제
    }
}
