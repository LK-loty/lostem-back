package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;*/
    @Column
    private Long hostUserId;

    @Column
    private String hostUserTag;

    @Column
    private Long guestUserId;

    @Column
    private String guestUserTag;

    @Column
    private String type;



    @OneToMany(mappedBy = "lostChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageLost> chatMessages;



    public static LostChatRoom createChatRoom(Long postId, String host, String guest) {
        return LostChatRoom.builder()
                .postId(postId)
                .hostUserTag(host)
                .guestUserTag(guest)
                .type("Lost")
                .build();
    }

    public String getLastMessage() {
        return chatMessages.stream()
                .reduce((first, second) -> second) // 목록에서 마지막 요소 가져오기
                .map(ChatMessageLost::getMessage)
                .orElse(null);
    }

    public LocalDateTime getLastMessageTime() {
        return chatMessages.stream()
                .map(ChatMessageLost::getTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }
}
