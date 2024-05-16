package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReportDTO;
import loty.lostem.dto.ReportInfoDTO;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;
    private final ChatRoomRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createReport(ReportDTO reportDTO, Long reporterId) {
        Long userId = userRepository.findByTag(reportDTO.getUserTag()).get().getUserId();

        boolean exists = reportRepository.existsByUserIdAndTypeAndLocation(userId, reportDTO.getType(), reportDTO.getLocation());

        if(exists) {
            return "exist";
        }

        Report created = Report.createReport(reportDTO, userId, reporterId);

        User user = userRepository.findById(userId).get();
        user.updateReport();
        userRepository.save(user);

        reportRepository.save(created);

        return "OK";
    }

    public List<ReportInfoDTO> readAll() {
        return reportRepository.findAll().stream()
                .map(this::reportToDTO)
                .collect(Collectors.toList());
    }



    public ReportInfoDTO reportToDTO(Report report) {
        return ReportInfoDTO.builder()
                .type(report.getType())
                .location(report.getLocation())
                .userId(report.getUserId())
                .reportId(report.getReportId())
                .title(report.getTitle())
                .contents(report.getContents())
                .time(report.getTime())
                .build();
    }
}
