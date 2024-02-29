package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.dto.ChatRoomListDTO;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomLostRepository roomLostRepository;
    private final ChatRoomFoundRepository roomFoundRepository;
    private final ChatMessageLostRepository messageLostRepository;
    private final ChatMessageFoundRepository messageFoundRepository;
    private final UserRepository userRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;

    // 채팅
    @Transactional
    public ChatRoomDTO createRoom(ChatMessageDTO messageDTO, Long userId) {
        User guest = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No guest"));
        ChatRoomDTO chatRoomDTO = null;

        //Optional<? extends ChatRoom> existingRoom = roomRepository.findByHostUserAndGuestUserAndPostLost(host, guest, (PostLost) post);
        if (messageDTO.getPostType().equals("Lost")) {
            log.info("lost 게시글");
            PostLost post = lostRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            LostChatRoom chatRoom = roomLostRepository.findByPostIdAndGuestUserId(messageDTO.getPostId(), userId);

            if (chatRoom == null) {
                log.info("채팅방을 생성합니다");
                chatRoom = LostChatRoom.createChatRoom(post.getPostId(), host.getUserId(), guest.getUserId());
                roomLostRepository.save(chatRoom); // user 마다 채팅방 리스트화하면 저장할 때 힘듦+검색은 비교적 편함? 지금 로직은 생성할 때 바로 저장+검색은 오래 걸릴수도?

                ChatMessageLost chatMessage = ChatMessageLost.createChatMessage(messageDTO, chatRoom, guest);
                messageLostRepository.save(chatMessage);
            }
            return roomToDTO(chatRoom);
        }
        else if (messageDTO.getPostType().equals("Found")){
            log.info("found 게시물");
            PostFound post = foundRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            FoundChatRoom chatRoom = roomFoundRepository.findByPostIdAndGuestUserId(host.getUserId(), userId);

            if (chatRoom == null) {
                log.info("채팅방을 생성합니다.");
                chatRoom = FoundChatRoom.createChatRoom(post.getPostId(), host.getUserId(), guest.getUserId());
                roomFoundRepository.save(chatRoom);

                ChatMessageFound chatMessage = ChatMessageFound.createChatMessage(messageDTO, chatRoom, guest);
                messageFoundRepository.save(chatMessage);
            }
            return roomToDTO(chatRoom);
        }
        return null;
    }

    // 채탕방 목록 정보
    public ChatRoomListDTO getAllRooms(Long userId) {
        // 상대방이랑 host, guest 바뀌는거 생각해서 상대방의 이미지, 닉네임, 태그 전달
        List<LostChatRoom> lostChatRooms = roomLostRepository.findByHostUserIdOrGuestUserId(userId);
        List<FoundChatRoom> foundChatRooms = roomFoundRepository.findByHostUserIdOrGuestUserId(userId);

        List<ChatRoomDTO> lostDTOs = lostChatRooms.stream()
                .map(this::roomToDTO).toList();
        List<ChatRoomDTO> foundDTOs = foundChatRooms.stream()
                .map(this::roomToDTO).toList();

        return ChatRoomListDTO.builder()
                .chatRoomLostList(lostDTOs)
                .chatRoomFoundList(foundDTOs)
                .build();
    }

    // 특정 채팅방 정보만. 메시지는 아직 >> 같이?? 채팅방 위에 게시물 정보 같이 전달(채팅방 정보)
    /*public ChatRoomDTO selectRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        return roomToDTO(chatRoom);
    }*/



    // 메시지
    @Transactional
    public ChatMessageDTO createLostMessage(ChatMessageDTO chatMessageDTO, Long userId) {
        LostChatRoom chatRoom = roomLostRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No room found for the provide id"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provide id"));
        ChatMessageLost newMessage = ChatMessageLost.createChatMessage(chatMessageDTO, chatRoom, user);
        messageLostRepository.save(newMessage);
        return chatMessageDTO;
    }
    @Transactional
    public ChatMessageDTO createFoundMessage(ChatMessageDTO chatMessageDTO, Long userId) {
        FoundChatRoom chatRoom = roomFoundRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No room found for the provide id"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provide id"));
        ChatMessageFound newMessage = ChatMessageFound.createChatMessage(chatMessageDTO, chatRoom, user);
        messageFoundRepository.save(newMessage);
        return chatMessageDTO;
    }



    // 채팅방의 모든 메시지 가져오기
    /*public List<ChatMessageDTO> getAllMessages(Long roomId) { // 쿼리 작성 필요
        return chatMessageRepository.findAll().stream()
                .map(this::messageToDTO)
                .collect(Collectors.toList());
    }*/



    public ChatRoomDTO roomToDTO (LostChatRoom chatRoom) {
        String lastMessage = null;
        LocalDateTime time = null;

        if (chatRoom.getChatMessages() != null) {
            lastMessage = chatRoom.getLastMessage();
            time = chatRoom.getLastMessageTime();
        }

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserId(chatRoom.getHostUserId())
                .guestUserId(chatRoom.getGuestUserId())
                .type(chatRoom.getType())
                .postId(chatRoom.getPostId())
                .lastMessage(lastMessage)
                .time(time)
                .build();
    }
    public ChatRoomDTO roomToDTO (FoundChatRoom chatRoom) {
        String lastMessage = null;
        LocalDateTime time = null;

        if (chatRoom.getChatMessages() != null) {
            lastMessage = chatRoom.getLastMessage();
            time = chatRoom.getLastMessageTime();
        }

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserId(chatRoom.getHostUserId())
                .guestUserId(chatRoom.getGuestUserId())
                .type(chatRoom.getType())
                .postId(chatRoom.getPostId())
                .lastMessage(lastMessage)
                .time(time)
                .build();
    }

    public ChatMessageDTO messageToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .sender(message.getSender().getUserId())
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }
}