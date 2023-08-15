package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChattingMessageDTO {
    private Long chatting_message_id;
    private String message;

    private LocalDateTime time;
}
