package loty.lostem.repository;

import loty.lostem.entity.LostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostReportRepository extends JpaRepository<LostReport, Long> {
}
