package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.service.S3ImageService;
import loty.lostem.service.TokenService;
import loty.lostem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final S3ImageService imageService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok("회원가입 완료");
    }

    // find, pw >> 본인 인증 먼저 하고 나서

    @PostMapping("/find")
    public ResponseEntity<String> findUsername(@Valid @RequestBody AStringDTO phone) {
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
    public ResponseEntity<UserPreviewDTO> userPreview(@RequestParam String tag) {
        UserPreviewDTO userPreviewDTO = userService.previewUser(tag);

        if (userPreviewDTO != null) {
            return ResponseEntity.ok(userPreviewDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<UserDetailDTO> selectUser(HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        UserDetailDTO dto = userService.readUser(userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(HttpServletRequest request,
                                         @Valid @RequestPart("data") UserUpdateDTO userDTO,
                                         @RequestPart(value = "image", required = false)MultipartFile image) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String url = null;
        if (image != null && !image.isEmpty()) {
            url = imageService.upload(image, "user");
        }

        String check = userService.updateUser(userId, userDTO, url);
        if (check.equals("OK")) {
            return ResponseEntity.ok("정보 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/change")
    public ResponseEntity<String> change(HttpServletRequest request, @RequestBody AStringDTO password) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String check = userService.changePassword(userId, password.getWord());
        if (check.equals("OK")) {
            return ResponseEntity.ok("비밀번호 변경 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(HttpServletRequest request, @RequestBody AStringDTO password) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String check = userService.deleteUser(userId, password.getWord());
        if (check.equals("OK")) {
            return ResponseEntity.ok("유저 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
