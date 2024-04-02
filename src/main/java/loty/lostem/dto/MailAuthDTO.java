package loty.lostem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MailAuthDTO {
    private String email;
    private String authCode;

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
