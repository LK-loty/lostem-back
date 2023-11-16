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
    private Long appraisal_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @NotNull
    private Long appraisal_user;

    @Column
    @NotNull
    @Size(max = 100)
    private String contents;

    @Column
    @NotNull
    private LocalDateTime time;
}
