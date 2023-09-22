package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostDTO;
import loty.lostem.entity.Post;
import loty.lostem.entity.User;
import loty.lostem.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    // 하나의 게시물에 대한 정보 리턴
    public PostDTO readPost(Long postId) {
        Post selectPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostDTO selectedDTO = postToDTO(selectPost);
        return selectedDTO;
    }

    // 전체 목록 보기
    public List<PostDTO> allPosts() {
        return postRepository.findAll().stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    // 검색 필터를 받아서 결과 게시물들 리턴. 구현 필요
    public List<PostDTO> searchPost(PostDTO postDTO) {
        return postRepository.findAll().stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
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

    @Transactional
    public PostDTO deletePost(Long postId) {
        Post selectedPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostDTO selectedDTO = postToDTO(selectedPost);
        postRepository.deleteById(postId);
        return selectedDTO;
    }



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
