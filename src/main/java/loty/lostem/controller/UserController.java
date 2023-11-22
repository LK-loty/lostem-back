package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.UserDTO;
import loty.lostem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users") // 세션은 따로 /login
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> selectUser(@PathVariable Long id) {
        UserDTO dto = userService.readUser(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody @Valid UserDTO userDTO) {
        userService.updateUser(userDTO);
        return ResponseEntity.ok("정보 수정 완료");
    }

    /*@GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDTO> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }*/
}
