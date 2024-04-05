package loty.lostem.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ChatMessageDTO;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    // Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리
    // redis 에 메시지가 발행되면 해당 메시지를 ChatMessageDTO로 변환하고 messaging template 이용하여 채팅방의 모든 웹소켓 클라이언트들에게 메시지 전달
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageDTO roomMessage = objectMapper.readValue(publishMessage, ChatMessageDTO.class);
            messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessageDTO chatMessageDTO = objectMapper.readValue(publishMessage, ChatMessageDTO.class);
            // Websocket 구독자에게 채팅 메시지 Send. 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageDTO.getRoomId(), chatMessageDTO);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

}