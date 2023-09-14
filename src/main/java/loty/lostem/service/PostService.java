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
    public PostDTO createPost(PostDTO getDTO, User user) {  // userDTO를 받아서 user 엔티티로 변환하고 전달할 것인가
        Post created = Post.createPost(getDTO, user);  // 변환하여 전달하려면 user 쪽의 변환 메소드 사용??
        postRepository.save(created);
        PostDTO createdDTO = postToDTO(created);
        return createdDTO;
    }

    public PostDTO readPost(Long postId) {
        Post selectPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostDTO selectedDTO = postToDTO(selectPost);
        return selectedDTO;
    }

    @Transactional
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        Post selectedPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        selectedPost.updatePostFields(selectedPost, postDTO);
        postRepository.save(selectedPost);
        PostDTO changedDTO = postToDTO(selectedPost);
        return changedDTO;
    }
    // 조회로 키 먼저 찾고 덮어쓰기? / read 후 상태에서 수정/삭제?




    public PostDTO postToDTO(Post post) {
        return PostDTO.builder()
                .post_id(post.getPost_id())
                .title(post.getTitle())
                .image(post.getImage())
                .type(post.getType())
                .period(post.getPeriod())
                .place(post.getPlace())
                .item(post.getItem())
                .explain(post.getExplain())
                .state(post.getStorage())
                .report(post.getReport())
                .time(post.getTime())
                .category(post.getCategory())
                .storage(post.getStorage())
                .build();
    }
}
