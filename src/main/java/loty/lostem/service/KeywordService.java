package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.entity.Keyword;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.User;
import loty.lostem.repository.KeywordRepository;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;

    @Transactional
    public KeywordDTO createKeyword(KeywordDTO keywordDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        // 한 번에 하나씩으로 변경
        Keyword created = Keyword.createKeyword(keywordDTO.getKeyword(), user);
        keywordRepository.save(created);
        return keywordDTO;
    }

    public List<KeywordDTO> readKeyword(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUser_UserId(userId);

        List<KeywordDTO> keywordDTOList = keywords.stream()
                .map(this::keywordToDTO)
                .collect(Collectors.toList());

        return keywordDTOList;
    }

    public List<KeywordListDTO> searchKeyword(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUser_UserId(userId);

        List<KeywordListDTO> listDTOS = new ArrayList<>();
        Set<Long> addedPostIds = new HashSet<>();

        for (Keyword keyword : keywords) {
            List<PostLost> lostList = lostRepository.findPostsAfterKeywordTime(keyword.getKeyword(), keyword.getTime());
            List<PostFound> foundList = foundRepository.findPostsAfterKeywordTime(keyword.getKeyword(), keyword.getTime());

            for (PostLost postLost : lostList) {
                if (!postLost.getUser().getUserId().equals(userId) && addedPostIds.add(postLost.getPostId())) {
                    listDTOS.add(listToDTO(postLost));
                }
            }
            for (PostFound postFound : foundList) {
                if (!postFound.getUser().getUserId().equals(userId) && addedPostIds.add(postFound.getPostId())) {
                    listDTOS.add(listToDTO(postFound));
                }
            }
        }

        Collections.sort(listDTOS, (a, b) -> a.getTime().compareTo(b.getTime()));

        /*return KeywordListDTO.builder()
                .postListDTOs(listDTOS)
                .build();*/
        return listDTOS;
    }

    @Transactional
    public KeywordDTO updateKeyword(KeywordDTO keywordDTO, Long userId) {
       keywordRepository.deleteByUser_UserId(userId);
       createKeyword(keywordDTO, userId);

       return keywordDTO;
    }

    @Transactional
    public KeywordDTO deleteKeyword(KeywordDTO keywordDTO, Long userId) {
        keywordRepository.deleteByKeywordAndUser_UserId(keywordDTO.getKeyword(), userId);

        return keywordDTO;
    }



    public KeywordDTO keywordToDTO(Keyword keyword) {
        return KeywordDTO.builder()
                .keyword(keyword.getKeyword())
                .time(keyword.getTime())
                .build();
    }

    public KeywordListDTO listToDTO(PostFound post) {
        List<String> image = post.getImages();
        return KeywordListDTO.builder()
                .postId(post.getPostId())
                .type("Found")
                .title(post.getTitle())
                .image(image.get(0))
                .area(post.getArea())
                .time(post.getTime())
                .build();
    }

    public KeywordListDTO listToDTO(PostLost post) {
        List<String> image = post.getImages();
        return KeywordListDTO.builder()
                .postId(post.getPostId())
                .type("Lost")
                .title(post.getTitle())
                .image(image.get(0))
                .area(post.getArea())
                .time(post.getTime())
                .build();
    }
}
