package loty.lostem.entity;

import jakarta.persistence.*;
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
    private String keyword;
}
