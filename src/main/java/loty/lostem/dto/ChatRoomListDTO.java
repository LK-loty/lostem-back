package loty.lostem.dto;

import lombok.*;
import loty.lostem.chat.MessageType;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomListDTO {
    private Long roomId;
    private MessageType messageType;
    private ChatUserInfoDTO chatUserDTO;
    private ChatLastMessageDTO chatMessageDTO;

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
