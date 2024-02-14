package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.entity.ChatMessage;
import loty.lostem.entity.ChatRoom;
import loty.lostem.entity.Post;
import loty.lostem.entity.User;
import loty.lostem.repository.ChatMessageRepository;
import loty.lostem.repository.ChatRoomRepository;
import loty.lostem.repository.PostRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 채팅방
    @Transactional
    public ChatRoomDTO createRoom(ChatMessageDTO messageDTO, Long userId) {
        /*if (chatRoomDTO.getPostId()) {
            // 유저가 이미 그 상대와 채팅방 만들었다면 그 채팅방으로 이동
        }*/
        User guest = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No guest"));
        Post post = postRepository.findById(messageDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No post"));
        User host = userRepository.findById(post.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No host"));

        ChatRoom chatRoom = ChatRoom.createChatRoom(host, guest, messageDTO.getPostId());
        chatRoomRepository.save(chatRoom); // user 마다 채팅방 리스트화하면 저장할 때 힘듦+검색은 비교적 편함? 지금 로직은 생성할 때 바로 저장+검색은 오래 걸릴수도?

        ChatMessage chatMessage = ChatMessage.createChatMessage(messageDTO, chatRoom, guest);
        chatMessageRepository.save(chatMessage);

        ChatRoomDTO chatRoomDTO = roomToDTO(chatRoom);
        return chatRoomDTO;
    }

    // 채탕방 목록 정보
    public List<ChatRoomDTO> getAllRooms(Long userId) {
        return chatRoomRepository.findByHostUser_UserId(userId).stream()
                .map(this::roomToDTO)
                .collect(Collectors.toList());
    }

    /*public List<ChatRoomDTO> findAllRoom() {  // 채팅방 최근 순서로 반환
        List chatRoomDTOList = chatRoomRepository.findAll();
        Collections.reverse(chatRoomDTOList);
        return chatRoomDTOList;
    }*/

    // 특정 채팅방 정보만. 메시지는 아직 >> 같이?? 채팅방 위에 게시물 정보 같이 전달(채팅방 정보)
    public ChatRoomDTO selectRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        return roomToDTO(chatRoom);
    }



    // 메시지
    public ChatMessageDTO createMessage(ChatMessageDTO chatMessageDTO, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No room found for the provide id"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provide id"));
        ChatMessage newMessage = ChatMessage.createChatMessage(chatMessageDTO, chatRoom, user);
        chatMessageRepository.save(newMessage);
        return chatMessageDTO;
    }

    // 채팅방의 모든 메시지 가져오기
    public List<ChatMessageDTO> getAllMessages(Long roomId) { // 쿼리 작성 필요
        return chatMessageRepository.findAll().stream()
                .map(this::messageToDTO)
                .collect(Collectors.toList());
    }



    public ChatRoomDTO roomToDTO(ChatRoom room) {
        return ChatRoomDTO.builder()
                .roomId(room.getRoomId())
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