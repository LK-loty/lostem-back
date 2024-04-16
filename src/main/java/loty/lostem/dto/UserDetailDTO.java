package loty.lostem.dto;

import lombok.Builder;

@Builder
public class UserDetailDTO {
    private String name;
    private String username;
    private String phone;
    private String email;

    private String nickname;

    private String profile;

    private double star;

    private String tag;
}
