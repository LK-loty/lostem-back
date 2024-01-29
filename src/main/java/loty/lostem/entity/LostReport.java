package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.PostReportDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class LostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private PostLost postLost;

    @Column
    @NotNull
    private Long userId;

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



    public static LostReport createPostReport(PostReportDTO postReportDTO, PostLost postlost, Long userId) {
        return LostReport.builder()
                .postLost(postlost)
                .userId(userId)
                .title(postReportDTO.getTitle())
                .contents(postReportDTO.getContents())
                .time(LocalDateTime.now())
                .build();
    }
}
