package loty.lostem.entity;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long appraisal_user;

    @Column
    @Size(max = 100)
    private String contents;

    @Column
    private LocalDateTime time;
}
