package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomDTO {
    private Long roomId;

    // 유저
    private String hostUserTag;  // 방 만든 사람 말고 게시글 주인
    //private Long hostUserId;

    private String guestUserTag;  // 채팅 시도하는 사람
    //private Long guestUserId;

    // 게시물
    private String postType;
}
