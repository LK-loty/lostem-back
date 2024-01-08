package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserImageDTO {
    private Long imageId;

    private Long userId;

    private String url;

    private String name;

    private String size;
}
