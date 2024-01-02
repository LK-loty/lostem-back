package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostReportDTO;
import loty.lostem.service.PostReportService;
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

    @PostMapping("/lost")
    public ResponseEntity<PostReportDTO> createLostReport(@Valid @RequestBody PostReportDTO reportDTO) {
        PostReportDTO dto = postReportService.createLostReport(reportDTO);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/found")
    public ResponseEntity<PostReportDTO> createFoundReport(@Valid @RequestBody PostReportDTO reportDTO) {
        PostReportDTO dto = postReportService.createFoundReport(reportDTO);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
