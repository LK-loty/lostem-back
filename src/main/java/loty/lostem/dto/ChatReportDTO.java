package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatReportDTO {
    private Long reportId;

    private Long roomId;  // 채팅방 키

    @NotNull
    @Size(max = 20)
    private String title;  // 제목

    @NotNull
    @Size(max = 50)
    private String contents;  // 신고 내용

    private LocalDateTime time;
}
