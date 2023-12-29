package loty.lostem.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class LoginDTO {
    @NotNull
    @Size(min = 3, max = 20)
    private String username;  // 아이디

    @NotNull
    @Size(min = 6, max = 20)
    private String password;  // 비밀번호
}
