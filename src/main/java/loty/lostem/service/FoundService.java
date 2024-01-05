package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostFoundDTO;
import loty.lostem.dto.PostFoundListDTO;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.User;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoundService {
    private static final String DEFAULT_IMAGE_URL = "static/lotylostem.png";
    private final PostFoundRepository postFoundRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostFoundDTO createPost(PostFoundDTO postFoundDTO) {
        User user = userRepository.findById(postFoundDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostFound created = PostFound.createPost(postFoundDTO, user);
        postFoundRepository.save(created);
        return postFoundDTO;
    }

    // 하나의 게시물에 대한 정보 리턴
    public PostFoundDTO readPost(Long postId) {
        PostFound selectPost = postFoundRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostFoundDTO selectedDTO = postToDTO(selectPost);
        return selectedDTO;
    }

    // 전체 목록 보기
    public Page<PostFoundListDTO> allLists(Pageable pageable) {
        return postFoundRepository.findAll(pageable)
                .map(this::listToDTO);
    }

    public List<PostFoundDTO> userPost(Long id) {
        return postFoundRepository.findByUser_UserId(id).stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostFoundDTO updatePost(PostFoundDTO postDTO) {
        PostFound selectedPost = postFoundRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        selectedPost.updatePostFields(postDTO);
        postFoundRepository.save(selectedPost);
        PostFoundDTO changedDTO = postToDTO(selectedPost);
        return changedDTO;
    }

    @Transactional
    public PostFoundDTO deletePost(Long postId) {
        PostFound selectedPost = postFoundRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostFoundDTO selectedDTO = postToDTO(selectedPost);
        postFoundRepository.deleteById(postId);
        return selectedDTO;
    }



    public PostFoundDTO postToDTO(PostFound post) {
        return PostFoundDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .images(post.getImages())
                .period(post.getPeriod())
                .area(post.getArea())
                .place(post.getPlace())
                .item(post.getItem())
                .contents(post.getContents())
                .state(post.getState())
                .report(post.getReport())
                .time(post.getTime())
                .category(post.getCategory())
                .build();
    }

    public PostFoundListDTO listToDTO(PostFound post) {
        return PostFoundListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .image(post.getImages())
                .area(post.getArea())
                .time(post.getTime())
                .build();
    }
}
