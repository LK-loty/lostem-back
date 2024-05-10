package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ReportDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String type;

    private Long location;

    @Column
    @NotNull
    private Long userId;

    private Long reporterId;

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



    public static PostReport createReport(ReportDTO reportDTO, Long userId, Long reporterId) {
        return PostReport.builder()
                .type(reportDTO.getType())
                .location(reportDTO.getLocation())
                .userId(userId)
                .reporterId(reporterId)
                .title(reportDTO.getTitle())
                .contents(reportDTO.getContents())
                .time(LocalDateTime.now())
                .build();
    }
}
