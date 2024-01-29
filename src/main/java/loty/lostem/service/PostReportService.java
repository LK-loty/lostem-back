package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.dto.PostReportDTO;
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
    public PostReportDTO createLostReport(PostReportDTO postReportDTO, Long userId) {
        PostLost post = lostRepository.findById(postReportDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided postId"));

        List<LostReport> list = lostReportRepository.findByPostLost_PostId(postReportDTO.getPostId());

        for (LostReport report : list) {
            if (report.getUserId().equals(userId)) {
                return null;
            }
        }

        if (!post.getUser().getUserId().equals(userId)) {
            LostReport created = LostReport.createPostReport(postReportDTO, post, userId);
            lostReportRepository.save(created);

            post.increaseCount();
            lostRepository.save(post);
        }
        return postReportDTO;
    }

    @Transactional
    public PostReportDTO createFoundReport(PostReportDTO postReportDTO, Long userId) {
        PostFound post = foundRepository.findById(postReportDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided postId"));

        List<FoundReport> list = foundReportRepository.findByPostFound_PostId(postReportDTO.getPostId());

        for (FoundReport report : list) {
            if (report.getUserId().equals(userId)) {
                return null;
            }
        }

        if (!post.getUser().getUserId().equals(userId)) {
            FoundReport created = FoundReport.createPostReport(postReportDTO, post, userId);
            foundReportRepository.save(created);

            post.increaseCount();
            foundRepository.save(post);
        }
        return postReportDTO;
    }



    // 어차피 수정은 못하고 삭제도 안 할 것 같으니
    /*@Transactional
    public ChattingReportDTO createChattingReport(ChattingDTO chattingDTO) {
    }*/
}
