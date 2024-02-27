package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
@DiscriminatorValue("POST")*/
//@MappedSuperclass
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostId")
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestId")
    private User guestUser;


    /*@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages;*/


    @Builder
    public static ChatRoom createChatRoom(User host, User guest) {
        return ChatRoom.builder()
                .host(host)
                .guest(guest)
                .build();
    }

    public ChatRoom(User hostUser, User guestUser) {
        this.hostUser = hostUser;
        this.guestUser = guestUser;
    }
}
