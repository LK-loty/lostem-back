package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostReportDTO {
    private Long postReportId;
    private Long postId;
    private String title;
    private String contents;

    private LocalDateTime time;
}
