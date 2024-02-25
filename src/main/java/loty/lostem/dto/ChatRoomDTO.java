package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomDTO {
    private Long roomId;

    private String hostUserTag;  // 방 만든 사람 말고 게시글 주인

    private String guestUserTag;  // 채팅 시도하는 사람

    private Long postId;

    private String lastMessage;
}
