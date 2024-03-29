package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatMessageDTO {  //(채팅방 대화 내역 = 기존 메시지 리스트), (게시물 id, 사진, 제목, 상태), (상대방 이미지, 닉네임, 태그)
    public enum MessageType{
        ENTER, TALK, LEAVE;
    }
    private MessageType type;

    private Long messageId;

    private Long roomId;  // 채팅방 키

    private String senderTag;  // 채팅 보낸 사람 태그
    private String receiverTag;

    private String postType; // 게시물 타입
    private Long postId;

    @NotNull
    @Size(max = 1000)
    private String message;  // 채팅 내용

    private LocalDateTime time;

    public void setMessageType(MessageType messageType) {
        this.type = messageType;
    }

    public void setReceiverTag(String receiverTag) {
        this.receiverTag = receiverTag;
    }
}
