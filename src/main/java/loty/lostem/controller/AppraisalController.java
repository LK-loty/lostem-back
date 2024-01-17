package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.AppraisalDTO;
import loty.lostem.service.AppraisalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appraisals")
public class AppraisalController {
    private final AppraisalService appraisalService;

    @PostMapping("/create")
    public ResponseEntity<AppraisalDTO> createAppraisal(@Valid @RequestBody AppraisalDTO appraisalDTO) {
        appraisalService.createAppraisal(appraisalDTO);
        if (appraisalDTO != null) {
            return ResponseEntity.ok(appraisalDTO);
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
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        AppraisalDTO dto = appraisalService.deleteAppraisal(id);
        if (dto != null) {
            return ResponseEntity.ok("평가글 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
