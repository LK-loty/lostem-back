package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReviewDTO;
import loty.lostem.service.ReviewService;
import loty.lostem.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(HttpServletRequest request, @Valid @RequestBody ReviewDTO reviewDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        ReviewDTO dto = reviewService.createReview(reviewDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<List<ReviewDTO>> userReview(@RequestParam String tag) {
        List<ReviewDTO> dtoList = reviewService.readReview(tag);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(HttpServletRequest request, @Valid @PathVariable Long id) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        ReviewDTO dto = reviewService.deleteReview(id, userId);
        if (dto != null) {
            return ResponseEntity.ok("평가글 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
