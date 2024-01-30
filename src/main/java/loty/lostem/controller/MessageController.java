package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ChatMessageDTO;
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

    // /pub/chat/message 로 요청이 들어오면 해당 메서드 실행 후 /sub/chat/room/id 구독하는 사용자들에게 요청할 때 같이온 chatRequest 실행
    @MessageMapping("/chat/message") // setApplicationDestinationPrefixes()를 통해 prefix "/pub"으로 설정 했으므로, 경로가 한번 더 수정되어 “/pub/chat/message”로 바뀜
    public void message(@Payload ChatMessageDTO message, @Header("token") String token) { // (@Payload ChatRequest chatRequest, SimpMessageHeaderAccessor headerAccessor)
        // 토큰 검사하면서 유저 추출? 유효성 검사 로직 정리 필요
        log.info("채팅");
        Long userId = tokenProvider.getUserId(token);
        if (ChatMessageDTO.MessageType.TALK.equals(message.getType())) {
            chatService.createMessage(message);
            simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        }

        /*// Redis를 통해 메시지를 발행하여 채팅방의 특정 토픽에 메시지 전송
        redisTemplate.convertAndSend("/topic/public", chatMessage);*/
    }

    @MessageMapping("/chat/{roomNo}")
    @SendTo("/sub/chat/{roomNo}") //
    public void broadcasting(final ChatMessageDTO messageDTO, @DestinationVariable(value = "roomNo") final String chatRoomNo) {
        chatService.createMessage(messageDTO);
    }

    /*@MessageMapping("/chat/{id}")
    public void sendMessage(@Payload MessageDTO messageDTO, @DestinationVariable Integer id){
        this.simpMessagingTemplate.convertAndSend("/queue/addChatToClient/"+id,messageDTO);
    }*/

    @MessageMapping("/chat/enter") // 처음 입장 시 메세지 출력
    public void enter(@Payload ChatMessageDTO message, @Header("token") String token) {
        Long userId = tokenProvider.getUserId(token);
        if (ChatMessageDTO.MessageType.ENTER.equals(message.getType())) {
            chatService.createMessage(message);
            simpMessageSendingOperations.convertAndSend("/sub/chat/room/"+message.getRoomId(),message.getSender()+"님이 입장하셨습니다");
        }
        System.out.println("채팅방 생성");
    }
    @GetMapping("/chat/get")
    public ResponseEntity<List<ChatMessageDTO>> getMessages(@PathVariable Long roomId) {
        // 채팅방에서의 모든 메시지를 가져오는 요청을 서비스에 전달하여 처리
        List<ChatMessageDTO> messages = chatService.getAllMessages(roomId);
        return ResponseEntity.ok(messages);
    }

    //
    /*@MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDTO chat, SimpMessageHeaderAccessor headerAccessor) {

        // 채팅방 유저+1
        repository.plusUserCnt(chat.getRoomId());

        // 채팅방에 유저 추가 및 UserUUID 반환
        String userUUID = repository.addUser(chat.getRoomId(), chat.getSender());

        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

        chat.setMessage(chat.getSender() + " 님 입장!!");
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }

    // 해당 유저
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatDTO chat) {
        log.info("CHAT {}", chat);
        chat.setMessage(chat.getMessage());
        template.convertAndSend("/sub/chat/room/" + chat.getRoomId(), chat);

    }

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
    }

    // 채팅에 참여한 유저 리스트 반환
    @GetMapping("/chat/userlist")
    @ResponseBody
    public ArrayList<String> userList(String roomId) {

        return repository.getUserList(roomId);
    }

    // 채팅에 참여한 유저 닉네임 중복 확인
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {

        // 유저 이름 확인
        String userName = repository.isDuplicateName(roomId, username);
        log.info("동작확인 {}", userName);

        return userName;
    }*/
}
