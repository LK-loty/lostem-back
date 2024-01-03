package loty.lostem.repository;

import loty.lostem.entity.PostLost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLostRepository extends JpaRepository<PostLost, Long> {
    List<PostLost> findByUser_UserId(Long userId);

    @Override
    Page<PostLost> findAll(Pageable pageable);
}
