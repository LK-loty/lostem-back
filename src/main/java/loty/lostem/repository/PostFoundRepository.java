package loty.lostem.repository;

import loty.lostem.entity.PostFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostFoundRepository extends JpaRepository<PostFound, Long> , JpaSpecificationExecutor<PostFound> {
    List<PostFound> findByUser_UserId(Long userId);

    @Override
    Page<PostFound> findAll(Pageable pageable);
}
