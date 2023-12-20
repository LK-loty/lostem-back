package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.entity.ChatMessage;
import loty.lostem.entity.ChatRoom;
import loty.lostem.entity.User;
import loty.lostem.repository.ChatMessageRepository;
import loty.lostem.repository.ChatRoomRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // 채팅방
    @Transactional
    public ChatRoomDTO createRoom(ChatRoomDTO chatRoomDTO) {
        User host = userRepository.findById(chatRoomDTO.getHostUserId())
                .orElseThrow(() -> new IllegalArgumentException("No host"));
        User guest = userRepository.findById(chatRoomDTO.getHostUserId())
                .orElseThrow(() -> new IllegalArgumentException("No guest"));
        ChatRoom chatRoom = ChatRoom.createChatRoom(chatRoomDTO, host, guest);
        chatRoomRepository.save(chatRoom); // user 마다 채팅방 리스트화하면 저장할 때 힘듦+검색은 비교적 편함? 지금 로직은 생성할 때 바로 저장+검색은 오래 걸릴수도?
        return chatRoomDTO;
    }

    public List<ChatRoomDTO> getAllRooms(Long userId) {
        return chatRoomRepository.findByHostUser_UserId(userId).stream()
                .map(this::roomToDTO)
                .collect(Collectors.toList());
    }

    // 특정 채팅방 정보만. 메시지는 아직 >> 같이??
    public ChatRoomDTO selectRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        return roomToDTO(chatRoom);
    }



    // 메시지
    public ChatMessageDTO createMessage(ChatMessageDTO chatMessageDTO) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No room found for the provide id"));
        User user = userRepository.findById(chatMessageDTO.getSender())
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provide id"));
        ChatMessage newMessage = ChatMessage.createChatMessage(chatMessageDTO, chatRoom, user);
        chatMessageRepository.save(newMessage);
        return chatMessageDTO;
    }

    // 채팅방의 모든 메시지 가져오기
    public List<ChatMessageDTO> getAllMessages(Long rommId) {
        return chatMessageRepository.findAll().stream()
                .map(this::messageToDTO)
                .collect(Collectors.toList());
    }



    public ChatRoomDTO roomToDTO(ChatRoom room) {
        return ChatRoomDTO.builder()
                .roomId(room.getRoomId())
                .report(room.getReport())
                .build();
    }

    public ChatMessageDTO messageToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }
}