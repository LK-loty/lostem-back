package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostDTO;
import loty.lostem.entity.Post;
import loty.lostem.entity.User;
import loty.lostem.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public PostDTO createPost(PostDTO getDTO, User user) {
        Post created = Post.createPost(getDTO, user);
        postRepository.save(created);
        PostDTO createdDTO = PostDTO.builder()
                .post_id(created.getPost_id())
                .title(getDTO.getTitle())
                .image(getDTO.getImage())
                .type(getDTO.getType())
                .period(getDTO.getPeriod())
                .place(getDTO.getPlace())
                .item(getDTO.getItem())
                .explain(getDTO.getExplain())
                .state(getDTO.getState())
                .report(getDTO.getReport())
                .time(getDTO.getTime())
                .category(getDTO.getCategory())
                .storage(getDTO.getStorage())
                .build();
        return createdDTO;
    }

}
