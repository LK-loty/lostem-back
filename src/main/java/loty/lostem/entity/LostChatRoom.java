package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Builder
//@DiscriminatorValue("LOST")
@Getter
public class LostChatRoom {
    @Id
    private Long roomId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_post_id", nullable = false)
    private PostLost postLost;*/
    @Column(name = "lost_post_id")
    private Long postId;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_user_id", nullable = false)
    private User guestUser;*/



    @OneToMany(mappedBy = "lostChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessageLost> chatMessages;


    public LostChatRoom(ChatRoom chatRoom, Long postId) {
        this.chatRoom = chatRoom;
        this.roomId = chatRoom.getRoomId();
        this.postId = postId;
    }
    /*public LostChatRoom(Long roomId, User hostUser, User guestUser, Long postId, List<ChatMessageLost> chatMessages) {
        super(roomId, hostUser, guestUser);
        this.postId = postId;
        this.chatMessages = chatMessages;
    }*/

    /*public static LostChatRoom createChatRoom(Long postId, User host, User guest) {
        return LostChatRoom.builder()
                .postId(postId)
                .hostUser(host)
                .guestUser(guest)
                //.postLost(postLost)
                .build();
    }*/
}
