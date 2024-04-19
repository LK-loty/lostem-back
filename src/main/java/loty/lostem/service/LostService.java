package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.User;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.UserRepository;
import loty.lostem.search.LostSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {
    private final PostLostRepository postLostRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createPost(PostLostDTO postLostDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostLost created = PostLost.createPost(postLostDTO, user);
        postLostRepository.save(created);
        return "OK";
    }

    // 하나의 게시물에 대한 정보 리턴
    public PostLostDetailsDTO readPost(Long postId) {
        PostLost selectPost = postLostRepository.findById(postId)
                .orElse(null);

        if (selectPost != null) {
            User user = userRepository.findById(selectPost.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No data found for user"));

            PostLostInfoDTO selectedDTO = postToDTO(selectPost);
            PostUserDTO readOnePost = postUserToDTO(user);

            PostLostDetailsDTO postLostDetailsDTO =
                    PostLostDetailsDTO.builder()
                            .postLostDTO(selectedDTO)
                            .postUserDTO(readOnePost)
                            .build();
            return postLostDetailsDTO;
        } else {
            return null;
        }
    }

    // 전체 목록 보기
    public Page<PostLostListDTO> allLists(Pageable pageable) {
        return postLostRepository.findAllNonDeleted(pageable)
                .map(this::listToDTO);
    }

    public List<PostLostInfoDTO> userPost(String tag) {
        return postLostRepository.findByUser_TagAndStateNot(tag, "삭제").stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String updatePost(Long userId, PostLostDTO postDTO) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));

        PostLost selectedPost = postLostRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (writer.getUserId().equals(selectedPost.getUser().getUserId())) {
            selectedPost.updatePostFields(postDTO);
            postLostRepository.save(selectedPost);

            return "OK";
        } else {
            return null;
        }
    }

    @Transactional
    public String updateState(Long userId, PostStateDTO stateDTO) {
        PostLost selectedPost = postLostRepository.findById(stateDTO.getPostId())
                .orElseThrow(()-> new IllegalArgumentException("No data found for the provided id"));

        if (!userId.equals(selectedPost.getUser().getUserId())) {
            return null;
        }

        selectedPost.updatePostState(stateDTO);
        postLostRepository.save(selectedPost);
        return "OK";
    }

    @Transactional
    public String deletePost(Long postId, Long userId) {
        PostLost selectedPost = postLostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (selectedPost.getUser().getUserId().equals(userId)) {
            selectedPost.deletePost(selectedPost);
            postLostRepository.save(selectedPost);
            return "OK";
        } else {
            return null;
        }
    }

    public Page<PostLostListDTO> search(String title, String category, LocalDateTime start, LocalDateTime end,
                                        String area, String place, String item, String contents, String state, Pageable pageable){
        Specification<PostLost> spec = (root, query, criteriaBuilder) -> null;

        if (title != null)
            spec = spec.and(LostSpecification.likeTitle(title));
        if (category != null)
            spec = spec.and(LostSpecification.equalCategory(category));
        if (start != null || end != null)
            spec = spec.and(LostSpecification.betweenPeriod(start, end));
        if (area != null)
            spec =spec.and(LostSpecification.likeArea(area));
        if (place != null)
            spec = spec.and(LostSpecification.likePlace(place));
        if (item != null)
            spec = spec.and(LostSpecification.equalItem(item));
        if (contents != null)
            spec = spec.and(LostSpecification.likeContents(contents));
        if (state != null)
            spec = spec.and(LostSpecification.equalState(state));

        spec = spec.and(LostSpecification.notDeleted());

        return postLostRepository.findAll(spec, pageable)
                .map(this::listToDTO);
    }

    public PostLostInfoDTO postToDTO(PostLost post) {
        return PostLostInfoDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .images(post.getImages())
                .date(post.getDate())
                .area(post.getArea())
                .place(post.getPlace())
                .item(post.getItem())
                .contents(post.getContents())
                .state(post.getState())
                .time(post.getTime())
                .category(post.getCategory())
                .build();
    }

    public PostLostListDTO listToDTO(PostLost post) {
        return PostLostListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .image(post.getImages())
                .area(post.getArea())
                .time(post.getTime())
                .build();
    }

    public PostUserDTO postUserToDTO(User user) {
        return PostUserDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .tag(user.getTag())
                .build();
    }
}
