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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {
    private final PostLostRepository postLostRepository;
    private final UserRepository userRepository;
    private final S3ImageService imageService;

    @Transactional
    public String createPost(PostLostDTO postLostDTO, Long userId, MultipartFile[] images) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostLost created = PostLost.createPost(postLostDTO, user);

        List<String> saveUrl = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                String url = imageService.upload(image, "lost");
                saveUrl.add(url);
            }
        }

        if (saveUrl == null || saveUrl.isEmpty()) {
            created.setBasicImage();
        } else {
            created.updateImage(saveUrl);
        }

        postLostRepository.save(created);
        return "OK";
    }

    // 하나의 게시물에 대한 정보 리턴
    public PostLostDetailsDTO readPost(Long postId) {
        PostLost selectPost = postLostRepository.findById(postId)
                .orElse(null);

        if (selectPost != null && !(selectPost.getState().equals("삭제"))) {
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
    public String updatePost(Long userId, PostLostDTO postDTO, MultipartFile[] images) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));

        PostLost selectedPost = postLostRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (writer.getUserId().equals(selectedPost.getUser().getUserId())) {
            List<String> saveUrl = new ArrayList<>();
            List<String> existingUrl = selectedPost.getImages();

            if ((postDTO.getImages() == null || postDTO.getImages().isEmpty())) {
                if (!(selectedPost.getImages().contains("https://lostem-upload.s3.amazonaws.com/itemBasic.png")) || selectedPost.getImages().isEmpty()) {
                    for (String deleteImg : existingUrl) {
                        imageService.deleteImageFromS3(deleteImg);
                    }
                }

                if (images != null && images.length > 0) {
                    for (MultipartFile image : images) {
                        String url = imageService.upload(image, "lost");
                        saveUrl.add(url);
                    }
                } else {
                    saveUrl.add("https://lostem-upload.s3.amazonaws.com/itemBasic.png");
                }

            } else {
                List<String> containList = postDTO.getImages();
                for (String deleteImage : existingUrl) {
                    if (!containList.contains(deleteImage)) {
                        imageService.deleteImageFromS3(deleteImage);
                    } else {
                        saveUrl.add(deleteImage);
                    }
                }

                if (images != null && images.length > 0) {
                    for (MultipartFile image : images) {
                        String url = imageService.upload(image, "lost");
                        saveUrl.add(url);
                    }
                }
            }

            selectedPost.updatePostFields(postDTO);
            selectedPost.updateImage(saveUrl);
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

            List<String> existingUrl = selectedPost.getImages();
            if (!(selectedPost.getImages().contains("https://lostem-upload.s3.amazonaws.com/itemBasic.png")) || selectedPost.getImages().isEmpty()) {
                for (String deleteImg : existingUrl) {
                    imageService.deleteImageFromS3(deleteImg);
                }
            }

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
        List<String> imgList = post.getImages();
        return PostLostInfoDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .images(imgList)
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
        List<String> image = post.getImages();
        return PostLostListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .image(image.get(0))
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
