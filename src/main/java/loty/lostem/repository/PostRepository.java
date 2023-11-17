package loty.lostem.repository;

import loty.lostem.entity.Post;
import loty.lostem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    //User findByUser_UserId(Long userId);
    List<Post> findByUser_UserId(Long userId);

}
