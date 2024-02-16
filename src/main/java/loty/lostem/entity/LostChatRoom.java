package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LostChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id", nullable = false)
    private PostLost postLost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestUserId")
    private User guestUser;



    @OneToMany(mappedBy = "lostChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageLost> chatMessages;



    public static LostChatRoom createChatRoom(User host, User guest, PostLost postLost) {
        return LostChatRoom.builder()
                .hostUser(host)
                .guestUser(guest)
                .postLost(postLost)
                .build();
    }
}
