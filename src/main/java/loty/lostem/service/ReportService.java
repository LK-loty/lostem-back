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

        boolean exists = reportRepository.existsByUserIdAndTypeAndLocation(reporterId, reportDTO.getType(), reportDTO.getLocation());

        if(exists) {
            return "exist";
        }

        Long userId = null;
        if (reportDTO.getType().equals("lost")) {
            userId = lostRepository.findById(reportDTO.getLocation()).get().getUser().getUserId();
        } else if (reportDTO.getType().equals("found")) {
            userId = foundRepository.findById(reportDTO.getLocation()).get().getUser().getUserId();
        } else if (reportDTO.getType().equals("chat")) {
            String userTag = userRepository.findById(reporterId).get().getTag();

            ChatRoom chatRoom = chatRepository.findById(reportDTO.getLocation()).get();

            if (chatRoom.getGuestUserTag().equals(userTag)) {
                userId = userRepository.findByTag(chatRoom.getHostUserTag()).get().getUserId();
            } else if (chatRoom.getHostUserTag().equals(userTag)) {
                userId = userRepository.findByTag(chatRoom.getGuestUserTag()).get().getUserId();
            }
        } else {
            return "type error";
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
