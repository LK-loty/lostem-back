package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @Size(max = 1000)
    private String message;

    @Column
    @NotNull
    private LocalDateTime time;
}
