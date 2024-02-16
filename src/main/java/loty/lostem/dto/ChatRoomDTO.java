package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomDTO {
    private Long roomId;

    private Long hostUserId;  // 방 만든 사람

    private String guestUserTag;  // 1:1 대화 상대

    private Long postId;

    private String lastMessage;
}
