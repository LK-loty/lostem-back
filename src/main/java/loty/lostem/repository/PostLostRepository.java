package loty.lostem.repository;

import loty.lostem.entity.PostLost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostLostRepository extends JpaRepository<PostLost, Long> , JpaSpecificationExecutor<PostLost> {
    List<PostLost> findByUser_TagAndStateNot(String tag, String state);

    @Override
    Page<PostLost> findAll(Pageable pageable);

    @Query("SELECT DISTINCT pf FROM PostLost pf WHERE pf.state <> '삭제'")
    Page<PostLost> findAllNonDeleted(Pageable pageable);

    @Query("SELECT DISTINCT pf FROM PostLost pf WHERE " +
            "(pf.title LIKE %:keyword% OR pf.contents LIKE %:keyword%) AND pf.time >= :keywordTime " +
            "AND pf.state <> '삭제'")
    List<PostLost> findPostsAfterKeywordTime(@Param("keyword") String keyword,
                                             @Param("keywordTime") LocalDateTime keywordTime);


}
