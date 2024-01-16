package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostUserDTO {
    private String nickname;

    private String profile;

    private String tag;
}
