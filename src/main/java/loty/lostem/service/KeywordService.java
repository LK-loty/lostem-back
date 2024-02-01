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

    @Autowired
    private LostService lostService;
    @Autowired
    private FoundService foundService;

    @Transactional
    public KeywordDTO createKeyword(KeywordDTO keywordDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        // 빈번한 검색이 있을 수 있고, 후에 테이블 확장성을 위해서 키워드 각각 저장
        for (String keyword : keywordDTO.getKeyword()) {
            Keyword created = Keyword.createKeyword(keyword, user);
            keywordRepository.save(created);
        }
        return keywordDTO;
    }

    public List<KeywordDTO> readKeyword(Long userId) {
        List<Keyword> keywords = keywordRepository.findByUser_UserId(userId);

        List<KeywordDTO> keywordDTOList = keywords.stream()
                .map(keyword -> KeywordDTO.builder()
                        .keywordId(keyword.getKeywordId())
                        .keyword(new String[]{keyword.getKeyword()})  // 배열로 변환하여 저장
                        .time(keyword.getTime())
                        .build())
                .collect(Collectors.toList());

        return keywordDTOList;
    }

    @Transactional
    public KeywordDTO updateKeyword(KeywordDTO keywordDTO, Long userId) {
       keywordRepository.deleteByUser_UserId(userId);
       createKeyword(keywordDTO, userId);

       return keywordDTO;
    }

    @Transactional
    public KeywordDTO deleteKeyword(KeywordDTO keywordDTO, Long userId) {
        for (String keyword : keywordDTO.getKeyword()) {
            keywordRepository.deleteByKeywordAndUser_UserId(keyword, userId);
        }

        return keywordDTO;
    }


}
