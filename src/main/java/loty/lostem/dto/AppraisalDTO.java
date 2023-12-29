package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class AppraisalDTO {
    private Long appraisalId;

    private Long userId;  // 평가받는 사람

    private Long appraisalUser;  // 평가하는 사람

    @NotNull
    @Size(max = 100)
    private String contents;  // 내용

    private LocalDateTime time;
}
