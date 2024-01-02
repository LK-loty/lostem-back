package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostFoundDTO;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.service.FoundService;
import loty.lostem.service.LostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/found")
public class FoundController {
    private final FoundService foundService;

    @PostMapping("/create")
    public ResponseEntity<PostFoundDTO> createPost(@Valid @RequestPart("data") PostFoundDTO postFoundDTO, @RequestPart(value = "image", required = false) MultipartFile[] images) {
        PostFoundDTO dto = foundService.createPost(postFoundDTO);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read/{id}") // 해당 글에 대한 정보
    public ResponseEntity<PostFoundDTO> selectPost(@PathVariable Long id) {
        PostFoundDTO dto = foundService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read") // 전체 글 목록(10개 단위 나중에)
    public ResponseEntity<List<PostFoundDTO>> allPosts() {
        List<PostFoundDTO> dtoList = foundService.allPosts();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/read/user/{id}") // 사용자 관련 글 목록
    public ResponseEntity<List<PostFoundDTO>> userPost(@PathVariable Long id) {
        List<PostFoundDTO> dtoList = foundService.userPost(id);
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
    public ResponseEntity<String> update(@Valid @RequestBody PostFoundDTO postDTO) {
        PostFoundDTO dto = foundService.updatePost(postDTO);
        if (dto != null) {
            return ResponseEntity.ok("게시물 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        PostFoundDTO dto = foundService.deletePost(id);
        if (dto != null) {
            return ResponseEntity.ok("게시물 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}