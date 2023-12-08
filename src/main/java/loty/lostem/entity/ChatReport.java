package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ChatReportDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChatReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private ChatRoom roomId;

    @Column
    @NotNull
    @Size(max = 20)
    private String title;

    @Column
    @NotNull
    @Size(max = 50)
    private String contents;

    @Column
    @NotNull
    private LocalDateTime time;



    public static ChatReport createChatReport(ChatReportDTO chatReportDTO, ChatRoom chatRoom) {
        return ChatReport.builder()
                .roomId(chatRoom)
                .title(chatReportDTO.getTitle())
                .contents(chatReportDTO.getContents())
                .time(LocalDateTime.now())
                .build();
    }
}
