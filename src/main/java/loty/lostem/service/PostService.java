package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostDTO;
import loty.lostem.dto.PostFoundDTO;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.entity.Post;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.User;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.PostRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLostRepository postLostRepository;
    private final PostFoundRepository postFoundRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        Post created = Post.createPost(postDTO, user);  // 변환하여 전달하려면 user 쪽의 변환 메소드 사용??
        postRepository.save(created);
        return postDTO;
    }

    @Transactional
    public PostLostDTO createPost(PostLostDTO postLostDTO) {
        User user = userRepository.findById(postLostDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostLost created = PostLost.createPost(postLostDTO, user);
        postLostRepository.save(created);
        return postLostDTO;
    }

    @Transactional
    public PostFoundDTO createPost(PostFoundDTO postFoundDTO) {
        User user = userRepository.findById(postFoundDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostFound created = PostFound.createPost(postFoundDTO, user);
        postFoundRepository.save(created);
        return postFoundDTO;
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

    public List<PostDTO> userPost(Long id) {
        return postRepository.findByUser_UserId(id).stream()
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
    public PostDTO updatePost(PostDTO postDTO) {
        Post selectedPost = postRepository.findById(postDTO.getPostId())
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
                .postId(post.getPostId())
                .title(post.getTitle())
                .images(post.getImages())
                .type(post.getType())
                .period(post.getPeriod())
                .area(post.getArea())
                .place(post.getPlace())
                .item(post.getItem())
                .contents(post.getContents())
                .state(post.getState())
                .report(post.getReport())
                .time(post.getTime())
                .category(post.getCategory())
                .storage(post.getStorage())
                .build();
    }
}
