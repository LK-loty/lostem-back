package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostReportDTO;
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
    public ResponseEntity<PostReportDTO> createLostReport(HttpServletRequest request, @Valid @RequestBody PostReportDTO reportDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        PostReportDTO dto = postReportService.createLostReport(reportDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/found")
    public ResponseEntity<PostReportDTO> createFoundReport(HttpServletRequest request, @Valid @RequestBody PostReportDTO reportDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        PostReportDTO dto = postReportService.createFoundReport(reportDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
