package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostDTO;
import loty.lostem.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("/read")
    public ResponseEntity<List<PostDTO>> allPost() {
        List<PostDTO> dtoList = postService.allPosts();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<PostDTO> selectPost(@PathVariable Long id) {
        PostDTO dto = postService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read/search")
    public ResponseEntity<List<PostDTO>> searchPost(@PathVariable PostDTO postDTO) {
        List<PostDTO> dtoList = postService.searchPost(postDTO);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@PathVariable PostDTO postDTO) {
        postService.createPost(postDTO);
        if (postDTO != null) {
            return ResponseEntity.ok(postDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
