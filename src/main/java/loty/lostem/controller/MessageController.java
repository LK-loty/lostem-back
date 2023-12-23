package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final TokenProvider tokenProvider;
    private final ChatService chatService;

    // /pub/chat/message 로 요청이 들어오면 해당 메서드 실행 후 /sub/chat/room/id 구독하는 사용자들에게 요청할 때 같이온 chatRequest 실행
    @MessageMapping("/chat/message") // setApplicationDestinationPrefixes()를 통해 prefix "/pub"으로 설정 했으므로, 경로가 한번 더 수정되어 “/pub/chat/message”로 바뀜
    public void message(@Payload ChatMessageDTO message, @Header("token") String token) { // (@Payload ChatRequest chatRequest, SimpMessageHeaderAccessor headerAccessor)
        // 토큰 검사하면서 유저 추출? 유효성 검사 로직 정리 필요
        Long userId = tokenProvider.getUserId(token);
        chatService.createMessage(message);
        simpMessageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message);
    }

    /*@MessageMapping("/chat/enter") // 처음 입장 시 메세지 출력
    public void enter(@Payload ChatMessageDTO message) {
        if (ChatMessageDTO.MessageType.ENTER.equals(message.getType())) {
            chatService.createRoom();
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
    }*/
    @GetMapping("/chat/get")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        // 채팅방에서의 모든 메시지를 가져오는 요청을 서비스에 전달하여 처리
        List<ChatMessageDTO> messages = chatService.getAllMessages(roomId);
        return ResponseEntity.ok(messages);
    }
}
