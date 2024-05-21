package loty.lostem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserUpdateDTO {
    private String name;
    private String nickname;
    private String username;
    private String phone;
    private String email;
    private String profile;
}
