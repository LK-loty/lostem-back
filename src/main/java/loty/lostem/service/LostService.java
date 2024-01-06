package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.dto.PostLostListDTO;
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
    private static final String DEFAULT_IMAGE_URL = "static/lotylostem.png";
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
    public Page<PostLostListDTO> allLists(Pageable pageable) {
        return postLostRepository.findAll(pageable)
                .map(this::listToDTO);
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

    public Page<PostLostListDTO> search(String title, String category, LocalDateTime date, String area, String place, String item, String contents, String state, Pageable pageable){
        Specification<PostLost> spec = (root, query, criteriaBuilder) -> null;
        // Specification<PostLost> spec = Specification.where(LostSpecification.findByCategory(category));
        if (title != null)
            spec = spec.and(LostSpecification.likeTitle(title));
        if (category != null)
            spec = spec.and(LostSpecification.equalCategory(category));
        if (date != null)
            spec = spec.and(LostSpecification.equalDate(date));
        if (area != null)
            spec =spec.and(LostSpecification.equalArea(area));
        if (place != null)
            spec = spec.and(LostSpecification.likePlace(place));
        if (item != null)
            spec = spec.and(LostSpecification.equalItem(item));
        if (contents != null)
            spec = spec.and(LostSpecification.likeContents(contents));
        if (state != null)
            spec = spec.and(LostSpecification.equalState(state));

        return postLostRepository.findAll(spec, pageable)
                .map(this::listToDTO);
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

    public PostLostListDTO listToDTO(PostLost post) {
        return PostLostListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .image(post.getImages())
                .area(post.getArea())
                .time(post.getTime())
                .build();
    }
}
