package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.KeywordDTO;
import loty.lostem.dto.KeywordListDTO;
import loty.lostem.service.KeywordService;
import loty.lostem.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 본인 키워드 확인용
    @GetMapping("/read")
    public ResponseEntity<List<KeywordDTO>> readKeyword(HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<KeywordDTO> dtoList = keywordService.readKeyword(userId);
        if (dtoList != null ) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<KeywordListDTO> searchKeyword(HttpServletRequest request) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        KeywordListDTO listDTO = keywordService.searchKeyword(userId);
        if (listDTO != null ) {
            return ResponseEntity.ok(listDTO);
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

    @DeleteMapping("/delete")
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
