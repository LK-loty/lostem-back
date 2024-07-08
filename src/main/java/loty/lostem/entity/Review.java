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
    private String reviewedUserTag; // 리뷰를 해준 사용자

    @Column
    @NotNull
    private String role; // 글쓴이 || 거래자

    @Column
    @NotNull
    @Size(max = 100)
    private String contents;

    @Column
    @NotNull
    private String postType;

    @Column
    @NotNull
    private Long postId;

    @Column
    @NotNull
    private LocalDateTime time;

    public static Review createReview(ReviewDTO reviewDTO, User user, String tag, String nickname,String role) {
        return Review.builder()
                .user(user)
                .reviewedUserTag(tag)
                .role(role)
                .contents(reviewDTO.getContents())
                .postType(reviewDTO.getPostType())
                .postId(reviewDTO.getPostId())
                .time(LocalDateTime.now())
                .build();
    }
}
