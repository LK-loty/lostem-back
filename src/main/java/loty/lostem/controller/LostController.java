package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.dto.PostLostDetailsDTO;
import loty.lostem.dto.PostLostListDTO;
import loty.lostem.dto.PostStateDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.LostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    private final TokenProvider tokenProvider;

    @PostMapping("/create")
    public ResponseEntity<PostLostDTO> createPost(HttpServletRequest request, @Valid @RequestPart("data") PostLostDTO postLostDTO, @RequestPart(value = "image", required = false) MultipartFile[] images) {
        String authorization = request.getHeader("Authorization");
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                userId = tokenProvider.getUserId(token);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        PostLostDTO dto = lostService.createPost(postLostDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read/{id}") // 해당 글에 대한 정보
    public ResponseEntity<PostLostDetailsDTO> selectPost(@PathVariable Long id) {
        PostLostDetailsDTO dto = lostService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<Page<PostLostListDTO>> allLists(@RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostLostListDTO> listDTOS = lostService.allLists(pageable);
        return ResponseEntity.ok(listDTOS);
    }

    @GetMapping("/read/user") // 사용자 관련 글 목록
    public ResponseEntity<List<PostLostDTO>> userPost(@RequestParam String username) {
        List<PostLostDTO> dtoList = lostService.userPost(username);
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
    public ResponseEntity<String> update(HttpServletRequest request, @Valid @RequestPart("data") PostLostDTO postLostDTO, @RequestPart(value = "image", required = false) MultipartFile[] images) {
        String authorization = request.getHeader("Authorization");
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                userId = tokenProvider.getUserId(token);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        PostLostDTO dto = null;
        if (userId != null && userId.equals(postLostDTO.getUserId())) {
            dto = lostService.updatePost(postLostDTO);
        } // else >> id 가 같은데 오류나는 경우, id 달라서 나는 오류 구분? 아니면 delete 처럼 null 통일?
        if (dto != null) {
            return ResponseEntity.ok("게시물 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/change")
    public ResponseEntity<String> update(HttpServletRequest request, @RequestBody PostStateDTO stateDTO) {
        String authorization = request.getHeader("Authorization");
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                userId = tokenProvider.getUserId(token);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        PostLostDTO dto = lostService.updateState(userId, stateDTO);
        if (dto != null) {
            return ResponseEntity.ok("상태 수정 완료");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(HttpServletRequest request, @Valid @PathVariable Long id) {
        String authorization = request.getHeader("Authorization");
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                userId = tokenProvider.getUserId(token);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        PostLostDTO dto = lostService.deletePost(id, userId);
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
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "item", required = false) String item,
            @RequestParam(value = "contents", required = false) String contents,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostLostListDTO> listDTOS = lostService.search(title, category, start, end, area, place, item, contents, state, pageable);

        return ResponseEntity.ok(listDTOS);
    }
}
