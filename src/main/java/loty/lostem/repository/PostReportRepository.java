package loty.lostem.repository;

import loty.lostem.entity.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    Optional<PostReport> findByPost_PostId(Long postId);
}
