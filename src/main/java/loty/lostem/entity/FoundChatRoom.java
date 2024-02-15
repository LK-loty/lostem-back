package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("FOUND")
@Getter
public class FoundChatRoom extends ChatRoom {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_post_id", nullable = false, insertable = false, updatable = false)
    private PostFound postFound;

    @Builder
    public FoundChatRoom(Long roomId, User hostUser, User guestUser, Long postId, List<ChatMessage> chatMessages, PostFound postFound) {
        super(roomId, hostUser, guestUser, chatMessages);
        this.postFound = postFound;
    }

    // 생성 메서드
    public static FoundChatRoom createChatRoom(User host, User guest, PostFound postFound) {
        return FoundChatRoom.builder()
                .hostUser(host)
                .guestUser(guest)
                .postFound(postFound)
                .build();
    }
}
