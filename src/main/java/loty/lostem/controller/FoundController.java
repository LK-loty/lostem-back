package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostFoundDTO;
import loty.lostem.dto.PostFoundListDTO;
import loty.lostem.service.FoundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/read")
    public ResponseEntity<Page<PostFoundListDTO>> allLists(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostFoundListDTO> listDTOS = foundService.allLists(pageable);
        return ResponseEntity.ok(listDTOS);
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