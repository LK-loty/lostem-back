package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ReviewDTO;
import loty.lostem.dto.ReviewReturnDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.ReviewService;
import loty.lostem.service.TokenService;
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
    public ResponseEntity<String> createReview(HttpServletRequest request, @Valid @RequestBody ReviewDTO reviewDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().body("토큰 오류");
        }
        String check = reviewService.createReview(reviewDTO, userId);
        if (check.equals("OK")) {
            return ResponseEntity.ok("평가글 생성 완료");
        } else if (check.equals("Existing")) {
            return ResponseEntity.status(201).body("이미 평가글을 작성했습니다.");
        } else {
            return ResponseEntity.status(202).body("평가글을 작성할 수 없는 사람입니다.");
        }
    }

    @GetMapping("/read/{tag}")
    public ResponseEntity<List<ReviewReturnDTO>> userReview(@PathVariable String tag) {
        List<ReviewReturnDTO> dtoList = reviewService.readReview(tag);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(HttpServletRequest request, @Valid @RequestParam Long id) {
        String userTag = tokenProvider.getUserTag(request.getHeader("Authorization"));
        if (userTag == null) {
            return ResponseEntity.badRequest().build();
        }

        String check = reviewService.deleteReview(id, userTag);
        if (check.equals("OK")) {
            return ResponseEntity.ok("평가글 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
