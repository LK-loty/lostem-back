package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostDTO {
    private Long post_id;
    private String title;
    private String image;
    private String type;
    private String period;
    private String place;
    private String item;
    private String explain;
    private String state;
    private String category;
    private String storage;
    private int report;

    private LocalDateTime time;
}
