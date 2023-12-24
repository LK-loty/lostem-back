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
    public AppraisalDTO createAppraisal(AppraisalDTO appraisalDTO) {
        User user = userRepository.findById(appraisalDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided userId"));
        Appraisal created = Appraisal.createAppraisal(appraisalDTO, user);
        appraisalRepository.save(created);
        return appraisalDTO;
    }

    // 상세 보기는 지원하지 않음. 전체 목록만
    public List<AppraisalDTO> readAppraisal(Long userId) {
        return appraisalRepository.findByUser_UserId(userId).stream()
                .map(this::appraisalToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AppraisalDTO deleteAppraisal(Long appraisalId) {
        Appraisal selectedAppraisal = appraisalRepository.findById(appraisalId)
                .orElseThrow(() -> new IllegalArgumentException("No data found"));
        AppraisalDTO selectedDTO = appraisalToDTO(selectedAppraisal);
        appraisalRepository.deleteById(appraisalId);
        return selectedDTO;
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
