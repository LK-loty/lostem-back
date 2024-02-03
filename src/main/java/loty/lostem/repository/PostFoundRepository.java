package loty.lostem.repository;

import loty.lostem.entity.PostFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostFoundRepository extends JpaRepository<PostFound, Long> , JpaSpecificationExecutor<PostFound> {
    List<PostFound> findByUser_TagAndStateNot(String tag, String state);

    @Override
    Page<PostFound> findAll(Pageable pageable);

    @Query("SELECT DISTINCT pf FROM PostFound pf WHERE pf.state <> '삭제'")
    Page<PostFound> findAllNonDeleted(Pageable pageable);

    @Query("SELECT DISTINCT pf FROM PostFound pf JOIN pf.user u WHERE u.userId = :userId " +
            "AND (pf.title LIKE %:keyword% OR pf.contents LIKE %:keyword%) AND pf.time >= :keywordTime " +
            "AND pf.state <> '삭제'")
    List<PostFound> findPostsAfterKeywordTime(@Param("userId") Long userId,
                                              @Param("keyword") String keyword,
                                              @Param("keywordTime") LocalDateTime keywordTime);

}
