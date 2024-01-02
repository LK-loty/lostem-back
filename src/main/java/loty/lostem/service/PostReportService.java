package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.dto.PostReportDTO;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReportService {
    private final LostReportRepository lostReportRepository;
    private final PostLostRepository lostRepository;
    private final FoundReportRepository foundReportRepository;
    private final PostFoundRepository foundRepository;

    @Transactional
    public PostReportDTO createLostReport(PostReportDTO postReportDTO) {
        PostLost post = lostRepository.findById(postReportDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided postId"));
        LostReport created = LostReport.createPostReport(postReportDTO, post);
        lostReportRepository.save(created);
        return postReportDTO;
    }

    @Transactional
    public PostReportDTO createFoundReport(PostReportDTO postReportDTO) {
        PostFound post = foundRepository.findById(postReportDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided postId"));
        FoundReport created = FoundReport.createPostReport(postReportDTO, post);
        foundReportRepository.save(created);
        return postReportDTO;
    }



    // 어차피 수정은 못하고 삭제도 안 할 것 같으니
    /*@Transactional
    public ChattingReportDTO createChattingReport(ChattingDTO chattingDTO) {
    }*/
}
