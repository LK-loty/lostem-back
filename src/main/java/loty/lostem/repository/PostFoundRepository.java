package loty.lostem.repository;

import loty.lostem.entity.PostFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFoundRepository extends JpaRepository<PostFound, Long> {
    List<PostFound> findByUser_UserId(Long userId);

    @Override
    Page<PostFound> findAll(Pageable pageable);
}
