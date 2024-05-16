package loty.lostem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReportInfoDTO {
    private String type;
    private Long location;
    private Long userId;
    private Long reportId;
    private String title;
    private String contents;
    private LocalDateTime time;

}
