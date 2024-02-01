package loty.lostem.repository;

import loty.lostem.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByUser_UserId(Long userId);

    void deleteByUser_UserId(Long userId);

    void deleteByKeywordAndUser_UserId(String keyword, Long userId);
}
