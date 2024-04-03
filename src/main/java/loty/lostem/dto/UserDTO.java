package loty.lostem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.security.UserRole;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserDTO {
    private Long userId;

    @NotNull
    @Size(min =2, max = 10)
    private String name;  // 이름

    @NotNull
    @Size(min =2, max = 10)
    private String nickname;  // 닉네임

    @NotNull
    @Size(min =5, max = 20)
    private String username;  //  아이디

    @NotNull
    private String password;  // 비밀번호

    @NotNull
    @Size(max = 11)
    private String phone;  // 전화번호

    @NotNull
    @Size(max = 30)
    private String email;  // 이메일

    private String profile;  // 프로필 이미지(url)

    @Max(2)
    private double star;  // 별점

    private int starCount;  // 별점 평균을 위한 평가자 수

    private String tag;  // 고유태그

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
    public void setTag(String tag) {
        this.tag = tag;
    }
    public static void setRole(UserDTO userDTO) {
        userDTO.role = UserRole.USER;
    }

    public void defaultProfile(String url) {
        this.profile = url;
    }
}
