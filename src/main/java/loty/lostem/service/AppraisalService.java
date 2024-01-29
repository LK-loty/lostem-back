package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.AppraisalDTO;
import loty.lostem.entity.Appraisal;
import loty.lostem.entity.User;
import loty.lostem.repository.AppraisalRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppraisalService {
    private final AppraisalRepository appraisalRepository;
    private final UserRepository userRepository;

    @Transactional
    public AppraisalDTO createAppraisal(AppraisalDTO appraisalDTO, Long userId) {
        User user = userRepository.findByTag(appraisalDTO.getTag())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided userId"));

        if (!userId.equals(user.getUserId())) {
            Appraisal created = Appraisal.createAppraisal(appraisalDTO, user);
            appraisalRepository.save(created);
            AppraisalDTO createdDTO = appraisalToDTO(created);
            return createdDTO;
        } else {
            return null;
        }
    }

    // 상세 보기는 지원하지 않음. 전체 목록만
    public List<AppraisalDTO> readAppraisal(String tag) {
        return appraisalRepository.findByUser_Tag(tag).stream()
                .map(this::appraisalToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppraisalDTO deleteAppraisal(Long appraisalId, Long userId) {
        Appraisal selectedAppraisal = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new IllegalArgumentException("No data found"));

        if (selectedAppraisal.getAppraisalUser().equals(userId)) {
            AppraisalDTO selectedDTO = appraisalToDTO(selectedAppraisal);
            appraisalRepository.deleteById(appraisalId);
            return selectedDTO;
        } else {
            return null;
        }
    }

    public AppraisalDTO appraisalToDTO(Appraisal appraisal) {
        return AppraisalDTO.builder()
                .appraisalId(appraisal.getAppraisalId())
                .appraisalUser(appraisal.getAppraisalUser())
                .contents(appraisal.getContents())
                .time(appraisal.getTime())
                .build();
    }
}
