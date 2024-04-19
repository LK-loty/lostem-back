package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class KeywordListDTO {
    private Long postId;
    private String type;
    private String title;
    private String image;
    private String area;
    private LocalDateTime time;
}
