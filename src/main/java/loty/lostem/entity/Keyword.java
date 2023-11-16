package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keyword_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Size(max = 10)
    private String keyword;
}
