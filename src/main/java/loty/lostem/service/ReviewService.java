package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReviewDTO;
import loty.lostem.dto.ReviewReturnDTO;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.Review;
import loty.lostem.entity.User;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.ReviewRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;

    @Transactional
    public String createReview(ReviewDTO reviewDTO, Long userId) {
        User self = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));
        if (reviewDTO.getPostType().equals("lost")) {
            PostLost post = lostRepository.findById(reviewDTO.getPostId())
                    .orElse(null);
            if (post == null) {
                return "NO DATA";
            }

            if (!self.getTag().equals(post.getUser().getTag())) { // 요청자가 글쓴이가 아닐 때 (태그 없음)
                Review created = Review.createReview(reviewDTO, post.getUser(), self.getTag(), "거래자");
                reviewRepository.save(created);

                updateStar(post.getUser().getTag(), reviewDTO.getStar());
                return "OK";
            } else {
                User user = userRepository.findByTag(reviewDTO.getTag())
                        .orElseThrow(() -> new IllegalArgumentException("No user"));

                Review created = Review.createReview(reviewDTO, user, self.getTag(), "글쓴이");
                reviewRepository.save(created);

                updateStar(user.getTag(), reviewDTO.getStar());
                return "OK";
            }
        } else if (reviewDTO.getPostType().equals("found")) {
            PostFound post = foundRepository.findById(reviewDTO.getPostId())
                    .orElse(null);
            if (post == null) {
                return "NO DATA";
            }

            if (!self.getTag().equals(post.getUser().getTag())) {
                Review created = Review.createReview(reviewDTO, post.getUser(), self.getTag(), "거래자");
                reviewRepository.save(created);

                updateStar(post.getUser().getTag(), reviewDTO.getStar());
                return "OK";
            } else {
                User user = userRepository.findByTag(reviewDTO.getTag())
                        .orElseThrow(() -> new IllegalArgumentException("No user"));

                Review created = Review.createReview(reviewDTO, user, self.getTag(), "글쓴이");
                reviewRepository.save(created);

                updateStar(user.getTag(), reviewDTO.getStar());
                return "OK";
            }
        } else
            return null;
    }

    // 상세 보기는 지원하지 않음. 전체 목록만
    public List<ReviewReturnDTO> readReview(String tag) {
        return reviewRepository.findByUser_Tag(tag).stream()
                .map(this::reviewToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteReview(Long reviewId, String userTag) {
        Review selectedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("No data found"));

        if (selectedReview.getReviewedUser().equals(userTag)) {
            reviewRepository.deleteById(reviewId);
            return "OK";
        } else {
            return null;
        }
    }

    @Transactional
    public void updateStar(String userTag, float star) {
        User user = userRepository.findByTag(userTag)
                .orElseThrow(() -> new IllegalArgumentException("No user"));
        user.updateStar(star);
        userRepository.save(user);
    }



    public ReviewReturnDTO reviewToDTO(Review review) {
        return ReviewReturnDTO.builder()
                .reviewedUserTag(review.getReviewedUser())
                .role(review.getRole())
                .contents(review.getContents())
                .time(review.getTime())
                .build();
    }
}
