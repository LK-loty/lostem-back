package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChattingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatting_report_id;

    @ManyToOne
    @JoinColumn(name = "chatting_id")
    private Chatting chatting;

    @Column
    @Size(max = 20)
    private String title;

    @Column
    @Size(max = 50)
    private String contents;

    @Column
    private LocalDateTime time;
}
