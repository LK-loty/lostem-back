package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import loty.lostem.chat.MessageType;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatMessageInfoDTO {
    private Long roomId;
    private String senderTag;
    private MessageType messageType;
    private String message;
    private LocalDateTime time;

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
