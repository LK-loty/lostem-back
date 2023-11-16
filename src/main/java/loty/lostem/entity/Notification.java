package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @Size(max = 20)
    private String notification;

    @Column
    private LocalDateTime time;
}
