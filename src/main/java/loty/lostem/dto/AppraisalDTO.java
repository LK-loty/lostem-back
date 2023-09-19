package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class AppraisalDTO {
    private Long appraisal_id;
    private Long appraisal_user;
    private String contents;
    private LocalDateTime time;
}
