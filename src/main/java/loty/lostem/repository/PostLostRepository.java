package loty.lostem.repository;

import loty.lostem.entity.PostLost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLostRepository extends JpaRepository<PostLost, Long> {
    List<PostLost> findByUser_UserId(Long userId);
}
