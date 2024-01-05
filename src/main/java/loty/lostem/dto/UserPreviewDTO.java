package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserPreviewDTO {
    private Long userId;

    private String nickname;

    private String profile;

    private float star;

    private String tag;
}
