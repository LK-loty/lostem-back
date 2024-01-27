package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import loty.lostem.dto.ChatRoomDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type")
@DiscriminatorValue("POST")
@Builder
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User hostUser;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestUserId")
    private User guestUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @Column
    @NotNull
    @Max(2)
    private int report;



    @OneToMany(mappedBy = "chatRoom")
    private List<ChatReport> chatReports = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages;



    public static ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO, User host, User guest) {
        return ChatRoom.builder()
                .report(0)
                .hostUser(host)
                .guestUser(guest)
                .build();
    }
}
