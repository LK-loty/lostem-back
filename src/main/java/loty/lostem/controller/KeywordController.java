package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.KeywordDTO;
import loty.lostem.service.KeywordService;
import loty.lostem.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/keyword")
public class KeywordController {
    private final KeywordService keywordService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<KeywordDTO> createKeyword(HttpServletRequest request, @Valid @RequestBody KeywordDTO keywordDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        KeywordDTO dto = keywordService.createKeyword(keywordDTO, userId);
        if (dto != null ) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<KeywordDTO> updateKeyword(HttpServletRequest request, @Valid @RequestBody KeywordDTO keywordDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        KeywordDTO dto = keywordService.updateKeyword(keywordDTO, userId);
        if (dto != null ) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<KeywordDTO> deleteKeyword(HttpServletRequest request, @Valid @RequestBody KeywordDTO keywordDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        KeywordDTO dto = keywordService.deleteKeyword(keywordDTO, userId);
        if (dto != null ) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
