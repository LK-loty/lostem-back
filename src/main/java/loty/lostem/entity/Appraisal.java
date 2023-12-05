package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.AppraisalDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Appraisal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appraisalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @NotNull
    private User user;

    @Column
    @NotNull
    private Long appraisalUser;

    @Column
    @NotNull
    @Size(max = 100)
    private String contents;

    @Column
    @NotNull
    private LocalDateTime time;

    public static Appraisal createAppraisal(AppraisalDTO appraisalDTO, User user) {
        return Appraisal.builder()
                .user(user)
                .appraisalUser(user.getUserId())
                .contents(appraisalDTO.getContents())
                .time(LocalDateTime.now())
                .build();
    }
}
