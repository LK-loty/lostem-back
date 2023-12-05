package loty.lostem.repository;

import loty.lostem.entity.Appraisal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppraisalRepository extends JpaRepository<Appraisal, Long> {
    Optional<Appraisal> findByUser_UserId(Long userId);
}
