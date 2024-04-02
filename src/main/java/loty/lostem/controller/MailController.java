package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.MailAuthDTO;
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

}
