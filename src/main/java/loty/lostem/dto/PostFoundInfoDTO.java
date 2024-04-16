package loty.lostem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
public class PostFoundInfoDTO {
    private Long postId;
    private String title;
    private String images;
    private String category;
    private LocalDateTime date;
    private String area;
    private String place;
    private String item;
    private String contents;
    private String state;
    private String storage;
    private LocalDateTime time;
}
