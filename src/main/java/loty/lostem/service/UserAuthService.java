package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.MailAuthDTO;
import loty.lostem.entity.User;
import loty.lostem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final UserRepository userRepository;

    private final MailService mailService;

    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public void sendCodeToEmail(MailAuthDTO toEmailDTO) {
        String email = this.checkDuplicatedEmail(toEmailDTO.getEmail());
        if (email.equals(toEmailDTO.getEmail())) {
            String authCode = this.createCode();
            toEmailDTO.setAuthCode(authCode);

            mailService.sendEmail(toEmailDTO);
            redisService.setValues(AUTH_CODE_PREFIX + toEmailDTO.getEmail(), authCode, Duration.ofMillis(authCodeExpirationMillis));
            log.info("인증코드 : " + authCode);
        } else {
            log.info("중복된 이메일이 존재합니다");
        }
    }

    public String validateEmail(MailAuthDTO mailAuthDTO) {
        String email = this.checkDuplicatedEmail(mailAuthDTO.getEmail());
        String code = mailAuthDTO.getAuthCode();

        if (email.equals(mailAuthDTO.getEmail())) {
            String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
            boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(code);

            if (authResult) {
                return "OK";
            } else {
                return "Fail";
            }
        }
        return "Other user";
    }

    private String checkDuplicatedEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getEmail();
        } else {
            return email;
        }
    }

    @Transactional
    private String createCode() {
        int lenth = 6;

        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
