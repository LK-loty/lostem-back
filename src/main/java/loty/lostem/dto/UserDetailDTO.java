package loty.lostem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDetailDTO {
    private String name;
    private String username;
    private String phone;
    private String email;
    private String nickname;
}
