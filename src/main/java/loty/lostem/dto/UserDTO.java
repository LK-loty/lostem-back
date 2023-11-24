package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserDTO {
    private Long userId;
    private String name;
    private String nickname;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String profile;
    private float star;
    private int starCount;
    private String tag;

    /*public UserSessionDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.modifiedDate = user.getModifiedDate();
    }*/

    public static void setPasswordEncode(UserDTO userDTO, String encode) {
        userDTO.password = encode;
    }
    public static void setPasswordNull(UserDTO userDTO) {
        userDTO.password = null;
    }
}
