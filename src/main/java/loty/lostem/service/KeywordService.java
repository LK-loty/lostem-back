package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.KeywordDTO;
import loty.lostem.dto.KeywordListDTO;
import loty.lostem.dto.PostFoundListDTO;
import loty.lostem.dto.PostLostListDTO;
import loty.lostem.entity.Keyword;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.User;
import loty.lostem.repository.KeywordRepository;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;

    private final LostService lostService;

    private final FoundService foundService;

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

    public KeywordListDTO searchKeyword(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUser_UserId(userId);

        List<PostLostListDTO> lostListDTOS = new ArrayList<>();
        List<PostFoundListDTO> foundListDTOS = new ArrayList<>();

        for (Keyword keyword : keywords) {
            List<PostLost> lostList = lostRepository.findPostsAfterKeywordTime(userId, keyword.getKeyword(), keyword.getTime());
            List<PostFound> foundList = foundRepository.findPostsAfterKeywordTime(userId, keyword.getKeyword(), keyword.getTime());

            for (PostLost postLost : lostList) {
                lostListDTOS.add(lostService.listToDTO(postLost));
            }
            for (PostFound postFound : foundList) {
                foundListDTOS.add(foundService.listToDTO(postFound));
            }
        }

        return KeywordListDTO.builder()
                .postLostDTO(lostListDTOS)
                .postFoundDTO(foundListDTOS)
                .build();
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
}
