package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String contents;
}
