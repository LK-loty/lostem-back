package loty.lostem.repository;

import loty.lostem.entity.LostReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostReportRepository extends JpaRepository<LostReport, Long> {
    List<LostReport> findByPostLost_PostId(Long postId);
}
