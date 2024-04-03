package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ReviewDTO;
import loty.lostem.dto.StarDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.ReviewService;
import loty.lostem.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;

    @PostMapping("/create")
    public ResponseEntity<ReviewDTO> createReview(HttpServletRequest request, @Valid @RequestBody ReviewDTO reviewDTO) {
        String userTag = tokenProvider.getUserTag(request.getHeader("Authorization"));
        if (userTag == null) {
            return ResponseEntity.badRequest().build();
        }

        ReviewDTO dto = reviewService.createReview(reviewDTO, userTag);
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
        String userTag = tokenProvider.getUserTag(request.getHeader("Authorization"));
        if (userTag == null) {
            return ResponseEntity.badRequest().build();
        }

        ReviewDTO dto = reviewService.deleteReview(id, userTag);
        if (dto != null) {
            return ResponseEntity.ok("평가글 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PatchMapping("/star")
    public ResponseEntity updateStar(HttpServletRequest request, @Valid @RequestBody StarDTO starDTO) {
        log.info("별점 줌 : " + starDTO.getStar());
        reviewService.updateStar(starDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
