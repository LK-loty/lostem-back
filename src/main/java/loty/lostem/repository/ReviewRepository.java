package loty.lostem.repository;

import loty.lostem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUser_TagOrderByTimeDesc(String tag);

    Optional<Review> findByReviewedUserTagAndPostTypeAndPostId(String tag, String postType, Long postId);
}
