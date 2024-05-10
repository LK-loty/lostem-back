package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReportDTO;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReportService {
    private final LostReportRepository lostReportRepository;
    private final PostLostRepository lostRepository;
    private final FoundReportRepository foundReportRepository;
    private final PostFoundRepository foundRepository;

    @Transactional
    public ReportDTO createLostReport(ReportDTO reportDTO, Long userId) {
        PostLost post = lostRepository.findById(reportDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided postId"));

        List<LostReport> list = lostReportRepository.findByPostLost_PostId(reportDTO.getPostId());

        for (LostReport report : list) {
            if (report.getUserId().equals(userId)) {
                return null;
            }
        }

        if (!post.getUser().getUserId().equals(userId)) {
            LostReport created = LostReport.createPostReport(reportDTO, post, userId);
            lostReportRepository.save(created);

            post.increaseCount();
            lostRepository.save(post);
        }
        return reportDTO;
    }

    @Transactional
    public ReportDTO createFoundReport(ReportDTO reportDTO, Long userId) {
        PostFound post = foundRepository.findById(reportDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided postId"));

        List<FoundReport> list = foundReportRepository.findByPostFound_PostId(reportDTO.getPostId());

        for (FoundReport report : list) {
            if (report.getUserId().equals(userId)) {
                return null;
            }
        }

        if (!post.getUser().getUserId().equals(userId)) {
            FoundReport created = FoundReport.createPostReport(reportDTO, post, userId);
            foundReportRepository.save(created);

            post.increaseCount();
            foundRepository.save(post);
        }
        return reportDTO;
    }



    // 어차피 수정은 못하고 삭제도 안 할 것 같으니
    /*@Transactional
    public ChattingReportDTO createChattingReport(ChattingDTO chattingDTO) {
    }*/
}
