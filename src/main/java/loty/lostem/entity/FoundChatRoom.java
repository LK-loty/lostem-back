package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "found_post_id", nullable = false)
    private PostFound postFound;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestUserId")
    private User guestUser;



    @OneToMany(mappedBy = "foundChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageFound> chatMessages;



    public static FoundChatRoom createChatRoom(User host, User guest, PostFound postFound) {
        return FoundChatRoom.builder()
                .hostUser(host)
                .guestUser(guest)
                .postFound(postFound)
                .build();
    }
}
