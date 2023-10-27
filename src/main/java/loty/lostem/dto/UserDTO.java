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
    private String username;
    private String password;
    private String phone;
    private String profile;
    private float star;
    private int start_count;
    private String tag;

    public static void setPasswordEncode(UserDTO userDTO, String encode) {
        userDTO.password = encode;
    }
}
