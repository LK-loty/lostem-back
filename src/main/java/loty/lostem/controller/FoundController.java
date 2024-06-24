package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.service.FoundService;
import loty.lostem.service.TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/found")
public class FoundController {
    private final FoundService foundService;
    private final TokenService tokenService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(HttpServletRequest request,
                                                   @Valid @RequestPart("data") PostFoundDTO postFoundDTO,
                                                   @RequestPart(value = "image", required = false) MultipartFile[] images) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String check = foundService.createPost(postFoundDTO, userId, images);

        if (check.equals("OK")) {
            return ResponseEntity.ok("게시물 생성 완료");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read/{id}") // 해당 글에 대한 정보
    public ResponseEntity<PostFoundDetailsDTO> selectPost(@PathVariable Long id) {
        if (id == null || id.equals(0L)) {
            return ResponseEntity.notFound().build();
        }

        PostFoundDetailsDTO dto = foundService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<Page<PostFoundListDTO>> allLists(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        Page<PostFoundListDTO> listDTOS = foundService.allLists(pageable);
        return ResponseEntity.ok(listDTOS);
    }

    @GetMapping("/read/user") // 사용자 관련 글 목록
    public ResponseEntity<List<PostFoundInfoDTO>> userPost(@RequestParam String tag) {
        List<PostFoundInfoDTO> dtoList = foundService.userPost(tag);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read/chat")
    public ResponseEntity<List<UserSimpleDTO>> chatUsers(HttpServletRequest request,
                                                  @RequestParam Long postId) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        List<UserSimpleDTO> userList = foundService.readChatUsers(userId, postId);

        if (userList != null) {
            return ResponseEntity.ok(userList);
        } else {
            return ResponseEntity.status(201).build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(HttpServletRequest request,
                                         @Valid @RequestPart("data") PostFoundDTO postFoundDTO,
                                         @RequestPart(value = "image", required = false) MultipartFile[] images) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String check = foundService.updatePost(userId, postFoundDTO, images);

        if (check.equals("OK")) {
            return ResponseEntity.ok("게시물 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/change")
    public ResponseEntity<String> change(HttpServletRequest request, @RequestBody PostStateDTO stateDTO) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String check = foundService.updateState(userId, stateDTO);
        if (check.equals("OK")) {
            return ResponseEntity.ok("상태 수정 완료");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(HttpServletRequest request, @Valid @PathVariable Long id) {
        Long userId = tokenService.getUserId(request);
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        String check = foundService.deletePost(id, userId);
        if (check.equals("OK")) {
            return ResponseEntity.ok("게시물 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostFoundListDTO>> searchLost(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "start", required = false) LocalDateTime start,
            @RequestParam(value = "end", required = false) LocalDateTime end,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "item", required = false) String item,
            @RequestParam(value = "contents", required = false) String contents,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "storage", required = false) String storage,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        Page<PostFoundListDTO> listDTOS = foundService.search(title, category, start, end, area, place, item, contents, state, storage, pageable);

        return ResponseEntity.ok(listDTOS);
    }
}