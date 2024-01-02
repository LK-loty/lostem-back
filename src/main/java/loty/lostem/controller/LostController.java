package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.service.LostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lost")
public class LostController {
    private final LostService lostService;

    @PostMapping("/create")
    public ResponseEntity<PostLostDTO> createPost(@Valid @RequestPart("data") PostLostDTO postLostDTO, @RequestPart("image") MultipartFile[] images) {
        PostLostDTO dto = lostService.createPost(postLostDTO);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read/{id}") // 해당 글에 대한 정보
    public ResponseEntity<PostLostDTO> selectPost(@PathVariable Long id) {
        PostLostDTO dto = lostService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read") // 전체 글 목록(10개 단위 나중에)
    public ResponseEntity<List<PostLostDTO>> allPosts() {
        List<PostLostDTO> dtoList = lostService.allPosts();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/read/user/{id}") // 사용자 관련 글 목록
    public ResponseEntity<List<PostLostDTO>> userPost(@PathVariable Long id) {
        List<PostLostDTO> dtoList = lostService.userPost(id);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@GetMapping("/read/search") // 검색 필터 적용 후 글 목록
    public ResponseEntity<List<PostLostDTO>> searchPost(@PathVariable PostLostDTO postDTO) {
        List<PostLostDTO> dtoList = lostService.searchPost(postDTO);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PatchMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody PostLostDTO postDTO) {
        PostLostDTO dto = lostService.updatePost(postDTO);
        if (dto != null) {
            return ResponseEntity.ok("게시물 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        PostLostDTO dto = lostService.deletePost(id);
        if (dto != null) {
            return ResponseEntity.ok("게시물 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
