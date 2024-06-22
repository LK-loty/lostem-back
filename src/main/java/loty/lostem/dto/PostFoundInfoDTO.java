package loty.lostem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class PostFoundInfoDTO {
    private Long postId;
    private String title;
    private List<String> images;
    private String category;
    private LocalDateTime date;
    private String area;
    private String place;
    private String item;
    private String contents;
    private String state;
    private String storage;
    private String traderTag;
    private LocalDateTime time;
}
