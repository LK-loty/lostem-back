package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private MessageType type; // 입장, 일반 메시지, 퇴장으로 나누는 메시지 타입

    private Long messageId;

    private Long roomId;  // 채팅방 키

    private Long sender;  // 채팅 보낸 사람 키

    @NotNull
    @Size(max = 1000)
    private String message;  // 채팅 내용

    private LocalDateTime time;
}
