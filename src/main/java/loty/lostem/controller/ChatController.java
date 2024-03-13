package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatMessageInfoDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.dto.ChatRoomListDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final TokenProvider tokenProvider;

    // 채팅방
    @PostMapping("/room/create")
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody ChatMessageDTO messageDTO, @RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatRoomDTO dto = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                dto = chatService.createRoom(messageDTO, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 채팅방 목록
    @GetMapping("/room/read/user")
    public ResponseEntity<ChatRoomListDTO> readUserRoom(@RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatRoomListDTO chatRoomListDTO= null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                chatRoomListDTO = chatService.getAllRooms(userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.ok(chatRoomListDTO);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/read/{roomId}")
    public ResponseEntity<ChatRoomDTO> selectRoom(@PathVariable Long roomId, @RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatRoomDTO chatRoomDTO = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                chatRoomDTO = chatService.selectRoom(roomId, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (chatRoomDTO != null) {
            return ResponseEntity.ok(chatRoomDTO);
        } else  {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/message/create")
    public ResponseEntity<ChatMessageDTO> createMessage(@RequestBody ChatMessageDTO messageDTO, @RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatMessageDTO dto = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);

                dto = chatService.createMessage(messageDTO, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 채팅방 채팅 내역
    @GetMapping("/get/room/{roomId}")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        // 채팅방에서의 모든 메시지를 가져오는 요청을 서비스에 전달하여 처리
        List<ChatMessageDTO> messages = chatService.getAllMessages(roomId);
        return ResponseEntity.ok(messages);
    }
}
