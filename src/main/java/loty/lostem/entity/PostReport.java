package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_report_id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private LocalDateTime time;
}
