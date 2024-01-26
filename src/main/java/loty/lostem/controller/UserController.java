package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.LoginDTO;
import loty.lostem.dto.UserDTO;
import loty.lostem.dto.UserPreviewDTO;
import loty.lostem.service.TokenService;
import loty.lostem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok("회원가입 완료");
    }

    // find, pw >> 본인 인증 먼저 하고 나서

    @PostMapping("/find")
    public ResponseEntity<String> findUsername(@Valid @RequestBody String phone) {
        String username = userService.findUser(phone);
        return ResponseEntity.ok(username);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody LoginDTO loginDTO) {
        userService.resetPassword(loginDTO);
        return ResponseEntity.ok("비밀번호 재설정 완료");
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkUsername(@RequestParam String username) {
        String checkUsername = userService.checkUsername(username);
        if (checkUsername != null) {
            return ResponseEntity.ok(checkUsername);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/preview")
    public ResponseEntity<UserPreviewDTO> userPreview(HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        UserPreviewDTO userPreviewDTO = userService.loginData(userId);

        if (userPreviewDTO != null) {
            return ResponseEntity.ok(userPreviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<UserDTO> selectUser(@RequestParam String tag) {
        UserDTO dto = userService.readUser(tag);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody UserDTO userDTO) {
        UserDTO dto = userService.updateUser(userDTO);
        if (dto != null) {
            return ResponseEntity.ok("정보 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @RequestBody UserDTO userDTO) {
        UserDTO dto = userService.deleteUser(userDTO);
        if (dto != null) {
            return ResponseEntity.ok("유저 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
