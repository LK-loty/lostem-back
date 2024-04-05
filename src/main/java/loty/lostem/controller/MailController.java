package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.MailAuthDTO;
import loty.lostem.dto.MailVerificationDTO;
import loty.lostem.service.UserAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/verify")
    public ResponseEntity verifyMail(@RequestParam String email, @RequestParam String code) {
        if (email == null) {
            return ResponseEntity.badRequest().build();
        }
        MailVerificationDTO result = userAuthService.verifyMail(email, code);
        if (result.isResult()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
