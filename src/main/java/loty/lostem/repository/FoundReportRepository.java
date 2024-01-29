package loty.lostem.repository;

import loty.lostem.entity.FoundReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoundReportRepository extends JpaRepository<FoundReport, Long> {
    List<FoundReport> findByPostFound_PostId(Long postId);
}
