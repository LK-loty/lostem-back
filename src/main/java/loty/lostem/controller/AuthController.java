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
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // 쿠키에서 리프레시 토큰 삭제 후 repository 에서도 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    tokenService.deleteRefreshToken(cookie.getValue());
                    break;
                }
            }
        }
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    @GetMapping("/access")
    public ResponseEntity<String> checkAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            if (tokenService.checkToken(token)) {
                return ResponseEntity.ok("유효한 토큰입니다");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Access 토큰입니다.");
            }
        }
        return ResponseEntity.badRequest().body("Access 토큰이 존재하지 않습니다.");
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> checkRefreshToken(@RequestParam String token) {
        if (tokenService.checkToken(token)) {
            return ResponseEntity.ok("유효한 토큰입니다");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh 토큰입니다.");
        }
    }
}
