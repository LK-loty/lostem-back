package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostDTO;
import loty.lostem.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestPart("data") PostDTO postDTO, @RequestPart("image") MultipartFile[] images) {
        postService.createPost(postDTO);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read/{id}") // 해당 글에 대한 정보
    public ResponseEntity<PostDTO> selectPost(@PathVariable Long id) {
        PostDTO dto = postService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read") // 전체 글 목록(10개 단위 나중에)
    public ResponseEntity<List<PostDTO>> allPosts() {
        List<PostDTO> dtoList = postService.allPosts();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/read/user/{id}") // 사용자 관련 글 목록
    public ResponseEntity<List<PostDTO>> userPost(@PathVariable Long id) {
        List<PostDTO> dtoList = postService.userPost(id);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read/search") // 검색 필터 적용 후 글 목록
    public ResponseEntity<List<PostDTO>> searchPost(@PathVariable PostDTO postDTO) {
        List<PostDTO> dtoList = postService.searchPost(postDTO);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody PostDTO postDTO) {
        PostDTO dto = postService.updatePost(postDTO);
        if (dto != null) {
            return ResponseEntity.ok("게시물 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        PostDTO dto = postService.deletePost(id);
        if (dto != null) {
            return ResponseEntity.ok("게시물 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
