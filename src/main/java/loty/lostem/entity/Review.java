package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ReviewDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @NotNull
    private User user;

    @Column
    @NotNull
    private Long reviewedUser; // 리뷰를 해준 사용자

    @Column
    @NotNull
    @Size(max = 100)
    private String contents;

    @Column
    @NotNull
    private LocalDateTime time;

    public static Review createReview(ReviewDTO reviewDTO, User user, Long userId) {
        return Review.builder()
                .user(user)
                .reviewedUser(userId)
                .contents(reviewDTO.getContents())
                .time(LocalDateTime.now())
                .build();
    }
}
