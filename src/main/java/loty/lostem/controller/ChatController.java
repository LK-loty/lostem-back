package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
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
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody ChatMessageDTO messageDTO, @RequestHeader("Authorization") String token) {
        Long userId = tokenProvider.getUserId(token);
        ChatRoomDTO dto = chatService.createRoom(messageDTO, userId);
        return ResponseEntity.ok(dto);
    }

    // 채팅방 목록
    @GetMapping("/room/read/user")
    public ResponseEntity<List<ChatRoomDTO>> readUserRoom(@RequestHeader("Authorization") String token) {
        Long userId = tokenProvider.getUserId(token);
        List<ChatRoomDTO> chatRoomDTOList = chatService.getAllRooms(userId);
        return ResponseEntity.ok(chatRoomDTOList);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/read/{roomId}")
    public ResponseEntity<ChatRoomDTO> selectRoom(@PathVariable Long roomId) {
        ChatRoomDTO chatRoomDTO = chatService.selectRoom(roomId);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 채팅방 채팅 내역
    @GetMapping("/chat/get")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        // 채팅방에서의 모든 메시지를 가져오는 요청을 서비스에 전달하여 처리
        List<ChatMessageDTO> messages = chatService.getAllMessages(roomId);
        return ResponseEntity.ok(messages);
    }
}
