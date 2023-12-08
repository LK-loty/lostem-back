package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ChatRoomDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostUserId")
    private User hostUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestUserId")
    private User guestUserId;

    @Column
    @NotNull
    @Size(max = 2)
    private int report;



    @OneToMany(mappedBy = "chatRoom")
    private List<ChatReport> chatReports = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages;



    public static ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO, User madeBy, User invited) {
        return ChatRoom.builder()
                .report(0)
                .hostUserId(madeBy)
                .guestUserId(invited)
                .build();
    }
}
