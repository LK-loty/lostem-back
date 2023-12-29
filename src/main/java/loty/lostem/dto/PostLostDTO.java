package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostLostDTO {
    private Long postId;
    private Long userId;
    private String title;
    private String images;
    private String category;
    private String period;
    private String area;
    private String place;
    private String item;
    private String contents;
    private String state;
    private int report;
    private LocalDateTime time;
}
