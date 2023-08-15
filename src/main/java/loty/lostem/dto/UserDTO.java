package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserDTO {
    private Long user_id;
    private String name;
    private String nickname;
    private String id;
    private String password;
    private String phone;
    private String profile;
    private float star;
    private String tag;
}
