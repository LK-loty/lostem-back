package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ChatMessageDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChatMessageLost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private LostChatRoom lostChatRoom;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User sender;

    @Column
    @NotNull
    @Size(max = 1000)
    private String message;

    @Column
    @NotNull
    private LocalDateTime time;



    public static ChatMessageLost createChatMessage(ChatMessageDTO chatMessageDTO, LostChatRoom lostChatRoom, User sender) {
        return ChatMessageLost.builder()
                .lostChatRoom(lostChatRoom)
                .sender(sender)
                .message(chatMessageDTO.getMessage())
                .time(LocalDateTime.now())
                .build();
    }
}
