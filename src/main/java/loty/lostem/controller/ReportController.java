package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReportDTO;
import loty.lostem.dto.ReportInfoDTO;
import loty.lostem.service.ReportService;
import loty.lostem.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<String> createReport(HttpServletRequest request, @Valid @RequestBody ReportDTO reportDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        String check = reportService.createReport(reportDTO, userId);
        if (check.equals("OK")) {
            return ResponseEntity.ok("신고글 작성 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<ReportInfoDTO>> readAll(HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<ReportInfoDTO> list = reportService.readAll();
        return ResponseEntity.ok(list);
    }
}
