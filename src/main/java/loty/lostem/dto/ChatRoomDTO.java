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
    private Long hostUserId;

    private String guestUserTag;  // 채팅 시도하는 사람
    private Long guestUserId;

    // 상대방
    private String profile;

    private String nickname;

    private String tag;

    // 게시물
    private String postType;

    private Long postId;

    private String postImage;

    private String title;

    private String state;

    // 메시지
    private String lastMessage;

    private LocalDateTime time;



    public void setTags(String hostUserTag, String guestUserTag) {
        this.hostUserTag = hostUserTag;
        this.guestUserTag = guestUserTag;
    }

    public void setCounterpart(String profile, String nickname, String tag) {
        this.profile = profile;
        this.nickname = nickname;
        this.tag = tag;
    }

    public void setPostData(Long postId, String postImage, String title, String state) {
        this.postId = postId;
        this.postImage = postImage;
        this.title = title;
        this.state = state;
    }
}
