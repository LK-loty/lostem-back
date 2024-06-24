package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.entity.ChatRoom;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.User;
import loty.lostem.repository.ChatRoomRepository;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.UserRepository;
import loty.lostem.search.FoundSpecification;
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
public class FoundService {
    private final PostFoundRepository postFoundRepository;
    private final UserRepository userRepository;
    private final S3ImageService imageService;
    private final ChatRoomRepository roomRepository;

    @Transactional
    public String createPost(PostFoundDTO postFoundDTO, Long userId, MultipartFile[] images) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostFound created = PostFound.createPost(postFoundDTO, user);

        List<String> saveUrl = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (MultipartFile image : images) {
                String url = imageService.upload(image, "found");
                saveUrl.add(url);
            }
        }

        if (saveUrl == null || saveUrl.isEmpty()) {
            created.setBasicImage();
        } else {
            created.updateImage(saveUrl);
        }

        postFoundRepository.save(created);
        return "OK";
    }

    // 하나의 게시물에 대한 정보 리턴
    public PostFoundDetailsDTO readPost(Long postId) {
        PostFound selectPost = postFoundRepository.findById(postId)
                .orElse(null);

        if (selectPost != null && !(selectPost.getState().equals("삭제"))) {
            User user = userRepository.findById(selectPost.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No data for user"));

            PostFoundInfoDTO selectedDTO = postToDTO(selectPost);
            PostUserDTO readOnePost = postUserToDTO(user);

            PostFoundDetailsDTO postFoundDetailsDTO =
                    PostFoundDetailsDTO.builder()
                            .postFoundDTO(selectedDTO)
                            .postUserDTO(readOnePost)
                            .build();
            return postFoundDetailsDTO;
        } else {
            return null;
        }
    }

    // 전체 목록 보기
    public Page<PostFoundListDTO> allLists(Pageable pageable) {
        return postFoundRepository.findAllNonDeleted(pageable)
                .map(this::listToDTO);
    }

    public List<PostFoundInfoDTO> userPost(String tag) {
        return postFoundRepository.findByUser_TagAndStateNot(tag, "삭제").stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String updatePost(Long userId, PostFoundDTO postDTO, MultipartFile[] images) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));

        PostFound selectedPost = postFoundRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

            if (writer.getUserId().equals(selectedPost.getUser().getUserId())) {
                List<String> saveUrl = new ArrayList<>();
                List<String> existingUrl = selectedPost.getImages();

                // 기존 이미지 제거
                if ((postDTO.getImages() == null || postDTO.getImages().isEmpty())) {
                    if (!(selectedPost.getImages().contains("https://lostem-upload.s3.amazonaws.com/itemBasic.png")) || selectedPost.getImages().isEmpty()) {
                        for (String deleteImg : existingUrl) {
                            imageService.deleteImageFromS3(deleteImg);
                        }
                    }

                    // 새 이미지 추가되는 경우
                    if (images != null && images.length > 0) {
                        for (MultipartFile image : images) {
                            String url = imageService.upload(image, "found");
                            saveUrl.add(url);
                        }
                    } else { // 둘 다 없으면 기본 이미지로
                        saveUrl.add("https://lostem-upload.s3.amazonaws.com/itemBasic.png");
                    }

                } else { // 기존 이미지 중 변경사항 적용
                    List<String> containList = postDTO.getImages();
                    for (String deleteImage : existingUrl) {
                        if (!containList.contains(deleteImage)) {
                            imageService.deleteImageFromS3(deleteImage);
                        } else {
                            saveUrl.add(deleteImage);
                        }
                    }

                    // 새 이미지 추가되는 경우
                    if (images != null && images.length > 0) {
                        for (MultipartFile image : images) {
                            String url = imageService.upload(image, "found");
                            saveUrl.add(url);
                        }
                    }
                }

                selectedPost.updatePostFields(postDTO);
                selectedPost.updateImage(saveUrl);
                postFoundRepository.save(selectedPost);

                return "OK";
            } else {
            return "Fail";
        }
    }

    @Transactional
    public String updateState(Long userId, PostStateDTO stateDTO) {
        PostFound selectedPost = postFoundRepository.findById(stateDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (!userId.equals(selectedPost.getUser().getUserId())) {
            return "Fail";
        }

        if (stateDTO.getState().equals("해결완료")) {
            selectedPost.setTraderTag(stateDTO.getTraderTag());
        }

        selectedPost.updatePostState(stateDTO);
        postFoundRepository.save(selectedPost);
        return "OK";
    }

    public List<UserSimpleDTO> readChatUsers(Long userId, Long postId) {
        List<ChatRoom> chatRooms = roomRepository.findByPostTypeAndPostId("found", postId);

        PostFound post = postFoundRepository.findById(postId).get();
        if (userId.equals(post.getUser().getUserId())) {

            List<UserSimpleDTO> userList = new ArrayList<>();
            if (chatRooms != null) {
                for (ChatRoom chatRoom : chatRooms) {
                    String tag = chatRoom.getGuestUserTag();
                    String nickname = userRepository.findByTag(tag).get().getNickname();

                    UserSimpleDTO dto = UserSimpleDTO.builder()
                            .nickname(nickname)
                            .tag(tag)
                            .build();

                    userList.add(dto);
                }

                return userList;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Transactional
    public String deletePost(Long postId, Long userId) {
        PostFound selectedPost = postFoundRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (selectedPost.getUser().getUserId().equals(userId)) {

            List<String> existingUrl = selectedPost.getImages();
            if (!(selectedPost.getImages().contains("https://lostem-upload.s3.amazonaws.com/itemBasic.png")) || selectedPost.getImages().isEmpty()) {
                for (String deleteImg : existingUrl) {
                    imageService.deleteImageFromS3(deleteImg);
                }
            }

            selectedPost.deletePost(selectedPost);
            postFoundRepository.save(selectedPost);
            return "OK";
        } else {
            return "Fail";
        }
    }

    public Page<PostFoundListDTO> search(String title, String category, LocalDateTime start, LocalDateTime end,
                                         String area, String place, String item, String contents, String state, String storage, Pageable pageable) {
        Specification<PostFound> spec = (root, query, criteriaBuilder) -> null;

        if (title != null)
            spec = spec.and(FoundSpecification.likeTitle(title));
        if (category != null)
            spec = spec.and(FoundSpecification.equalCategory(category));
        if (start != null || end != null)
            spec = spec.and(FoundSpecification.betweenPeriod(start, end));
        if (area != null)
            spec = spec.and(FoundSpecification.likeArea(area));
        if (place != null)
            spec = spec.and(FoundSpecification.likePlace(place));
        if (item != null)
            spec = spec.and(FoundSpecification.equalItem(item));
        if (contents != null)
            spec = spec.and(FoundSpecification.likeContents(contents));
        if (state != null)
            spec = spec.and(FoundSpecification.equalState(state));
        if (storage != null)
            spec = spec.and(FoundSpecification.likeStorage(storage));

        spec = spec.and(FoundSpecification.notDeleted());

        return postFoundRepository.findAll(spec, pageable)
                .map(this::listToDTO);
    }


    public PostFoundInfoDTO postToDTO(PostFound post) {
        List<String > imgList = post.getImages();
        return PostFoundInfoDTO.builder()
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
                .storage(post.getStorage())
                .traderTag(post.getTraderTag())
                .build();
    }

    public PostFoundListDTO listToDTO(PostFound post) {
        List<String> image = post.getImages();
        return PostFoundListDTO.builder()
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
