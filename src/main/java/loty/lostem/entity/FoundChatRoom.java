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
public class FoundChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_post_id", nullable = false)
    private PostFound postFound;*/
    @Column(nullable = false)
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;

    @Column
    private String type;



    @OneToMany(mappedBy = "foundChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageFound> chatMessages;



    public static FoundChatRoom createChatRoom(Long postId, User host, User guest) {
        return FoundChatRoom.builder()
                .postId(postId)
                .hostUser(host)
                .guestUser(guest)
                .type("Found")
                .build();
    }

    public String getLastMessage() {
        return chatMessages.stream()
                .reduce((first, second) -> second)
                .map(ChatMessageFound::getMessage)
                .orElse(null);
    }

    public LocalDateTime getLastMessageTime() {
        return chatMessages.stream()
                .map(ChatMessageFound::getTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }
}
