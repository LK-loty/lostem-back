package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatUserInfoDTO {
    private String profile;
    private String nickname;
    private String tag;

    public void setCounterpart(String profile, String nickname, String tag) {
        this.profile = profile;
        this.nickname = nickname;
        this.tag = tag;
    }
}
