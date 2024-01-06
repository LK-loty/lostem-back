package loty.lostem.repository;

import loty.lostem.entity.PostLost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostLostRepository extends JpaRepository<PostLost, Long> , JpaSpecificationExecutor<PostLost> {
    List<PostLost> findByUser_UserId(Long userId);

    @Override
    Page<PostLost> findAll(Pageable pageable);
}
