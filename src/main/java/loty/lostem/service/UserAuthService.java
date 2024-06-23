package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.LoginDTO;
import loty.lostem.dto.MailAuthDTO;
import loty.lostem.entity.User;
import loty.lostem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final UserRepository userRepository;

    private final MailService mailService;
    private final RedisService redisService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;


    public String sendCodeToEmail(MailAuthDTO toEmailDTO) {
        String authCode = this.createCode();
        toEmailDTO.setAuthCode(authCode);

        mailService.sendEmail(toEmailDTO);
        redisService.setValues(AUTH_CODE_PREFIX + toEmailDTO.getEmail(), authCode, Duration.ofMillis(authCodeExpirationMillis));
        log.info("인증코드 : " + authCode);
        return "OK";
    }

    public String sendAfterCheck(MailAuthDTO mailAuthDTO) {
        System.out.println("여기까진");
        if (this.findUsername(mailAuthDTO.getEmail()) == null) {
            this.sendCodeToEmail(mailAuthDTO);
            System.out.println("완료");
            return "OK";
        } else {
            System.out.println("실패");
            return "No user";
        }
    }

    public String validateEmail(MailAuthDTO mailAuthDTO) {
        String code = mailAuthDTO.getAuthCode();

        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + mailAuthDTO.getEmail());
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(code);

        if (authResult) {
            return "OK";
        } else {
            return "Fail";
        }
    }

    @Transactional
    public String findUser(MailAuthDTO mailAuthDTO) {
        String username = this.findUsername(mailAuthDTO.getEmail());

        if (username != null) {
            String check = validateEmail(mailAuthDTO);

            if (check.equals("OK")) {
                StringBuilder foundName = new StringBuilder(username.substring(0, 2));
                for (int i = 0; i < username.length() - 2; i++) {
                    foundName.append("*");
                }
                return foundName.toString();
            } else {
                return "Fail";
            }
        } else {
            return "No user";
        }
    }

    @Transactional
    public String resetPassword(LoginDTO loginDTO) {
        Optional<User> user = userRepository.findByUsername(loginDTO.getUsername());
        if (user.isPresent()) {
            User selectedUser = user.get();
            String encoded = bCryptPasswordEncoder.encode(loginDTO.getPassword());
            selectedUser.updatePassword(encoded);
            userRepository.save(selectedUser);

            return "OK";
        } else {
            return null;
        }
    }



    private String findUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get().getUsername();
        } else {
            return null;
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
