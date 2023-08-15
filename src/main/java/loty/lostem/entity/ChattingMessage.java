package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChattingMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatting_message_id;

    @ManyToOne
    @JoinColumn(name = "chatting_id")
    private Chatting chatting;

    @Column
    private String message;

    @Column
    private LocalDateTime time;
}
