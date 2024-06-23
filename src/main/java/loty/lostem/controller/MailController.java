package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.LoginDTO;
import loty.lostem.dto.MailAuthDTO;
import loty.lostem.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailController {
    private final UserAuthService userAuthService;

    @PostMapping("/request")
    public ResponseEntity sendMail(@RequestBody MailAuthDTO mailAuthDTO) {
        userAuthService.sendCodeToEmail(mailAuthDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity validateMail(@RequestBody MailAuthDTO mailAuthDTO) {
        String check = userAuthService.validateEmail(mailAuthDTO);
        if (check.equals("OK")) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(201));
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<String> singUpAuth(@RequestBody MailAuthDTO mailAuthDTO) {
        String check = userAuthService.sendAfterCheck(mailAuthDTO);
        
        if (check.equals("OK")) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.status(201).body("이메일이 이미 존재합니다");
        }
    }

    @PostMapping("/find") // 이메일 보내면 인증코드 확인 후 아이디랑 200 같이
    public ResponseEntity<String> findUsername(@Valid @RequestBody MailAuthDTO mailAuthDTO) {
        String username = userAuthService.findUser(mailAuthDTO);
        if (username.equals("No user")) {
            return ResponseEntity.status(201).body("일치하는 아이디가 없습니다");
        } else if (username.equals("Fail")) {
            return ResponseEntity.status(202).body("메일 인증에 실패했습니다.");
        } else {
            return ResponseEntity.ok(username);
        }
    }

    @PostMapping("/reset") // 이메일 인증 후
    public ResponseEntity<String> resetPassword(@Valid @RequestBody LoginDTO loginDTO) {
        String check = userAuthService.resetPassword(loginDTO);

        if (check.equals("OK")) {
            return ResponseEntity.ok("비밀번호 재설정 완료");
        } else {
            return ResponseEntity.status(201).body("없는 아이디 입니다.");
        }
    }
}
