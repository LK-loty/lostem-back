package loty.lostem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserDetailDTO {
    private String name;
    private String username;
    private String phone;
    private String email;
    private String nickname;
}
