package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReviewDTO;
import loty.lostem.entity.Review;
import loty.lostem.entity.User;
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

    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO, String userTag) {
        User user = userRepository.findByTag(reviewDTO.getTag())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided userId"));

        if (!userTag.equals(user.getTag())) {
            Review created = Review.createReview(reviewDTO, user, userTag);
            reviewRepository.save(created);
            ReviewDTO createdDTO = reviewToDTO(created);
            return createdDTO;
        } else {
            return null;
        }
    }

    // 상세 보기는 지원하지 않음. 전체 목록만
    public List<ReviewDTO> readReview(String tag) {
        return reviewRepository.findByUser_Tag(tag).stream()
                .map(this::reviewToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO deleteReview(Long reviewId, String userTag) {
        Review selectedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("No data found"));

        if (selectedReview.getReviewedUser().equals(userTag)) {
            ReviewDTO selectedDTO = reviewToDTO(selectedReview);
            reviewRepository.deleteById(reviewId);
            return selectedDTO;
        } else {
            return null;
        }
    }

    public ReviewDTO reviewToDTO(Review review) {
        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .tag(review.getUser().getTag())
                .reviewedUserTag(review.getReviewedUser())
                .contents(review.getContents())
                .time(review.getTime())
                .build();
    }
}
