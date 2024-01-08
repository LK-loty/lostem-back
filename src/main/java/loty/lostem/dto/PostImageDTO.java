package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostImageDTO {
    private Long imageId;

    private Long postId;

    private String url;

    private String name;

    private String size;
}
