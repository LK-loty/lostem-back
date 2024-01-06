package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.dto.PostLostListDTO;
import loty.lostem.service.LostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lost")
public class LostController {
    private final LostService lostService;

    @PostMapping("/create")
    public ResponseEntity<PostLostDTO> createPost(@Valid @RequestPart("data") PostLostDTO postLostDTO, @RequestPart(value = "image", required = false) MultipartFile[] images) {
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

    @GetMapping("/read")
    public ResponseEntity<Page<PostLostListDTO>> allLists(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostLostListDTO> listDTOS = lostService.allLists(pageable);
        return ResponseEntity.ok(listDTOS);
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

    /*@GetMapping("/read/search") // 검색 필터 적용 후 글 목록 ... >> 알림용 검색
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

    @GetMapping("/search")
    public ResponseEntity<Page<PostLostListDTO>> searchLost(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "period", required = false) LocalDateTime period,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "item", required = false) String item,
            @RequestParam(value = "contents", required = false) String contents,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostLostListDTO> listDTOS = lostService.search(title, category, period, area, place, item, contents, state, pageable);

        return ResponseEntity.ok(listDTOS);
    }
}
