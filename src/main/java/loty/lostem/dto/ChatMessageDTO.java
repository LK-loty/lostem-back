package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatMessageDTO {
    private Long messageId;
    private Long roomId;
    private String message;

    private LocalDateTime time;
}
