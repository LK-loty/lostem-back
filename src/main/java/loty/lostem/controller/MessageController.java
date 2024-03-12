package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.dto.ChatRoomListDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final TokenProvider tokenProvider;
    private final ChatService chatService;

    @MessageMapping("/chat/enter") // 처음 입장 시 메세지 출력
    public void enterRoom(@Payload ChatMessageDTO messageDTO, @Header("Authorization") String token) {
        Long userId = tokenProvider.getUserId(token);
        if (messageDTO.getRoomId() == null || messageDTO.equals("null")) {  // ChatMessageDTO.MessageType.ENTER.equals(messageDTO.getType())
            ChatRoomDTO roomDTO = chatService.createRoom(messageDTO, userId);
            simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + roomDTO.getRoomId(),messageDTO.getSenderTag()+"님이 입장하셨습니다. " + messageDTO.getMessage());
        }
        log.info("채팅방이 생성되었습니다");
    }

    // 채팅방 목록
    @MessageMapping("/chat/roomList")
    public void getRoomList(@Header("Authorization") String token) {
        Long userId = tokenProvider.getUserId(token);
        ChatRoomListDTO roomListDTO = chatService.getAllRooms(userId);

        simpMessageSendingOperations.convertAndSend("/sub/list/" + userId, roomListDTO);
    }

    // /pub/chat/message 로 요청이 들어오면 해당 메서드 실행 후 /sub/chat/room/id 구독하는 사용자들에게 요청할 때 같이온 chatRequest 실행
    @MessageMapping("/chat/message/create") // setApplicationDestinationPrefixes()를 통해 prefix "/pub"으로 설정 했으므로, 경로가 한번 더 수정되어 “/pub/chat/message”로 바뀜
    public void lostMessage(@Payload ChatMessageDTO messageDTO, @Header("Authorization") String token) { // (@Payload ChatRequest chatRequest, SimpMessageHeaderAccessor headerAccessor)
        // 토큰 검사하면서 유저 추출? 유효성 검사 로직 정리 필요
        tokenProvider.validateToken(token);
        log.info("유효한 토큰이므로 채팅을 계속합니다.");
        Long userId = tokenProvider.getUserId(token);

        //chatService.createLostMessage(messageDTO, userId);
        simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + messageDTO.getRoomId(), messageDTO);
        /*// Redis를 통해 메시지를 발행하여 채팅방의 특정 토픽에 메시지 전송
        redisTemplate.convertAndSend("/topic/public", chatMessage);*/
    }

    /*@MessageMapping("/chat/{roomId}")
    @SendTo("/sub/chat/{roomId}") //
    public void broadcasting(final ChatMessageDTO messageDTO, @DestinationVariable(value = "roomId") final Long roomId) {
        //////this.simpMessagingTemplate.convertAndSend("/queue/addChatToClient/"+id,messageDTO);
        chatService.createMessage(roomId, messageDTO, );
    }*/

    /*
    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        // 채팅방 유저 -1
        repository.minusUserCnt(roomId);

        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = repository.getUserName(roomId, userUUID);
        repository.delUser(roomId, userUUID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            // builder 어노테이션 활용
            ChatDTO chat = ChatDTO.builder()
                    .type(ChatDTO.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!!")
                    .build();

            template.convertAndSend("/sub/chat/room/" + roomId, chat);
        }
    }*/
}
