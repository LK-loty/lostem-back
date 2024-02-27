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

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id", nullable = false)
    private PostLost postLost;*/
    @Column
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;

    @Column
    private String type;



    @OneToMany(mappedBy = "lostChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageLost> chatMessages;



    public static LostChatRoom createChatRoom(Long postId, User host, User guest) {
        return LostChatRoom.builder()
                .postId(postId)
                .hostUser(host)
                .guestUser(guest)
                .type("Lost")
                .build();
    }
}
