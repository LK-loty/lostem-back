package loty.lostem.dto;

import lombok.*;
import loty.lostem.security.UserRole;

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
    private UserRole role;

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
    public static void setRole(UserDTO userDTO) {
        userDTO.role = UserRole.USER;
    }
}
