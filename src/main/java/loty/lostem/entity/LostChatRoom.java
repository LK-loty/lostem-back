package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Lost")
@Getter
public class LostChatRoom extends ChatRoom {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id", nullable = false, insertable = false, updatable = false)
    private PostLost postLost;

    @Builder
    public LostChatRoom(Long roomId, User hostUser, User guestUser, Long postId, List<ChatMessage> chatMessages, PostLost postLost) {
        super(roomId, hostUser, guestUser, chatMessages);
        this.postLost = postLost;
    }

    // 생성 메서드
    public static LostChatRoom createChatRoom(User host, User guest, PostLost postLost) {
        return LostChatRoom.builder()
                .hostUser(host)
                .guestUser(guest)
                .postLost(postLost)
                .build();
    }
}
