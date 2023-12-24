package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatMessageDTO {
    public enum MessageType{
        ENTER, TALK, LEAVE;
    }
    private MessageType type;

    private Long messageId;
    private Long roomId;
    private Long sender;
    private String message;

    private LocalDateTime time;
}
