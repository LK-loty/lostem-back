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
public class ChatMessageFound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private FoundChatRoom foundChatRoom;

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



    public static ChatMessageFound createChatMessage(ChatMessageDTO chatMessageDTO, FoundChatRoom foundChatRoom, User sender) {
        return ChatMessageFound.builder()
                .foundChatRoom(foundChatRoom)
                .sender(sender)
                .message(chatMessageDTO.getMessage())
                .time(LocalDateTime.now())
                .build();
    }
}
