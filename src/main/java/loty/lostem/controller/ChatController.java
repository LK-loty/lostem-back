package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.entity.ChatMessage;
import loty.lostem.entity.ChatRoom;
import loty.lostem.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    // 채팅방
    @PostMapping("/room/create")
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        chatService.createRoom(chatRoomDTO);
        return ResponseEntity.ok(chatRoomDTO);
    }

    // 채팅방 목록
    @GetMapping("/room/read/user/{id}")
    public ResponseEntity<List<ChatRoomDTO>> readUserRoom(@PathVariable Long userId) {
        List<ChatRoomDTO> chatRoomDTOList = chatService.getAllRooms(userId);
        return ResponseEntity.ok(chatRoomDTOList);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/read/{roomId}")
    public ResponseEntity<ChatRoomDTO> selectRoom(@PathVariable Long roomId) {
        ChatRoomDTO chatRoomDTO = chatService.selectRoom(roomId);
        return ResponseEntity.ok(chatRoomDTO);
    }

}
