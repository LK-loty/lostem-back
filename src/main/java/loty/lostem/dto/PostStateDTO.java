package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostStateDTO {
    private Long postId;
    private String state;
    private String traderTag;
}
