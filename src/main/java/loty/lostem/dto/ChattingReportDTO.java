package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChattingReportDTO {
    private Long chatting_report_id;
    private String contents;

    private LocalDateTime time;
}
