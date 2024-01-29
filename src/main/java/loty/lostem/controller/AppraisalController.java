package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.AppraisalDTO;
import loty.lostem.service.AppraisalService;
import loty.lostem.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appraisals")
public class AppraisalController {
    private final AppraisalService appraisalService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<AppraisalDTO> createAppraisal(HttpServletRequest request, @Valid @RequestBody AppraisalDTO appraisalDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        AppraisalDTO dto = appraisalService.createAppraisal(appraisalDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<AppraisalDTO>> userAppraisal(@RequestParam String tag) {
        List<AppraisalDTO> dtoList = appraisalService.readAppraisal(tag);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(HttpServletRequest request, @Valid @PathVariable Long id) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        AppraisalDTO dto = appraisalService.deleteAppraisal(id, userId);
        if (dto != null) {
            return ResponseEntity.ok("평가글 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
