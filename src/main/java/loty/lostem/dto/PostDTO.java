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
    private String category;
    private String period;
    private String field;
    private String place;
    private String item;
    private String explain;
    private String state;
    private int report;
    private String type;
    private String storage;

    private LocalDateTime time;
}
