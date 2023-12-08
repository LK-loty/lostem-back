package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatReportDTO {
    private Long reportId;
    private String title;
    private String contents;

    private LocalDateTime time;
}
