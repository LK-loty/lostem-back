package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class AppraisalDTO {
    private Long appraisalId;
    private Long appraisalUser;
    private String contents;
    private LocalDateTime time;
}
