package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.User;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {
    private final PostLostRepository postLostRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostLostDTO createPost(PostLostDTO postLostDTO) {
        User user = userRepository.findById(postLostDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostLost created = PostLost.createPost(postLostDTO, user);
        postLostRepository.save(created);
        return postLostDTO;
    }

    // 하나의 게시물에 대한 정보 리턴
    public PostLostDTO readPost(Long postId) {
        PostLost selectPost = postLostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostLostDTO selectedDTO = postToDTO(selectPost);
        return selectedDTO;
    }

    // 전체 목록 보기
    public List<PostLostDTO> allPosts() {
        return postLostRepository.findAll().stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    public List<PostLostDTO> userPost(Long id) {
        return postLostRepository.findByUser_UserId(id).stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostLostDTO updatePost(PostLostDTO postDTO) {
        PostLost selectedPost = postLostRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        selectedPost.updatePostFields(postDTO);
        postLostRepository.save(selectedPost);
        PostLostDTO changedDTO = postToDTO(selectedPost);
        return changedDTO;
    }

    @Transactional
    public PostLostDTO deletePost(Long postId) {
        PostLost selectedPost = postLostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        PostLostDTO selectedDTO = postToDTO(selectedPost);
        postLostRepository.deleteById(postId);
        return selectedDTO;
    }



    public PostLostDTO postToDTO(PostLost post) {
        return PostLostDTO.builder()
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
}
