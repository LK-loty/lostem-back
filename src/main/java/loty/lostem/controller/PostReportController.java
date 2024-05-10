package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReportDTO;
import loty.lostem.service.PostReportService;
import loty.lostem.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class PostReportController {
    private final PostReportService postReportService;
    private final TokenService tokenService;

    @PostMapping("/lost")
    public ResponseEntity<ReportDTO> createLostReport(HttpServletRequest request, @Valid @RequestBody ReportDTO reportDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        ReportDTO dto = postReportService.createLostReport(reportDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/found")
    public ResponseEntity<ReportDTO> createFoundReport(HttpServletRequest request, @Valid @RequestBody ReportDTO reportDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        ReportDTO dto = postReportService.createFoundReport(reportDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
