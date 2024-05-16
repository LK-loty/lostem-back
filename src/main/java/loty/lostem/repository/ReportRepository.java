package loty.lostem.repository;

import loty.lostem.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT COUNT(r) > 0 FROM Report r WHERE r.userId = :userId AND r.type = :type AND r.location = :location")
    boolean existsByUserIdAndTypeAndLocation(@Param("userId") Long userId, @Param("type") String type, @Param("location") Long location);
}
