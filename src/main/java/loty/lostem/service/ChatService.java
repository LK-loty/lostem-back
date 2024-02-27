package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.ChatMessageDTO;
import loty.lostem.dto.ChatRoomDTO;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

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

    // 채팅방
    @Transactional
    public ChatRoomDTO createRoom(ChatMessageDTO messageDTO, Long userId) {
        /*if (chatRoomDTO.getPostId()) {
            // 유저가 이미 그 상대와 채팅방 만들었다면 그 채팅방으로 이동
        }*/
        User guest = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No guest"));
        ChatRoomDTO chatRoomDTO = null;

        //Optional<? extends ChatRoom> existingRoom = roomRepository.findByHostUserAndGuestUserAndPostLost(host, guest, (PostLost) post);
        if (messageDTO.getPostType().equals("Lost")) {
            log.info("lost 게시글");
            //LostChatRoom chatRoom = roomLostRepository.findBy()

            PostLost post = lostRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            //LostChatRoom chatRoom = roomLostRepository.findByPostLost_IdAndHostUser_UserIdAndGuestUser_UserId(messageDTO.getPostId(), host.getUserId(), userId);
            LostChatRoom chatRoom = roomLostRepository.findByHostUser_UserIdAndGuestUser_UserId(host.getUserId(), userId);
            if (chatRoom == null) {
                log.info("채팅방을 생성합니다");
                chatRoom = LostChatRoom.createChatRoom(post.getPostId(), host, guest);
                roomLostRepository.save(chatRoom); // user 마다 채팅방 리스트화하면 저장할 때 힘듦+검색은 비교적 편함? 지금 로직은 생성할 때 바로 저장+검색은 오래 걸릴수도?

                ChatMessageLost chatMessage = ChatMessageLost.createChatMessage(messageDTO, chatRoom, guest);
                messageLostRepository.save(chatMessage);
            }
            return roomToDTO(chatRoom);
        } else if (messageDTO.getPostType().equals("Found")){
            log.info("found 게시물");
            PostFound post = foundRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            //FoundChatRoom chatRoom = roomFoundRepository.findByPostFound_IdAndHostUser_UserIdAndGuestUser_UserId(messageDTO.getPostId(), host.getUserId(), userId);
            FoundChatRoom chatRoom = roomFoundRepository.findByHostUser_UserIdAndGuestUser_UserId(host.getUserId(), userId);
            if (chatRoom == null) {
                log.info("채팅방을 생성합니다.");
                chatRoom = FoundChatRoom.createChatRoom(post.getPostId(), host, guest);
                roomFoundRepository.save(chatRoom);

                ChatMessageFound chatMessage = ChatMessageFound.createChatMessage(messageDTO, chatRoom, guest);
                messageFoundRepository.save(chatMessage);
            }
            return roomToDTO(chatRoom);
        }
        return null;
    }

    // 채탕방 목록 정보
    public List<ChatRoomDTO> getAllRooms(Long userId) {
        return /*chatRoomRepository.findByHostUser_UserId(userId).stream()
                .map(this::roomToDTO)
                .collect(Collectors.toList());*/ null;
    }

    /*public List<ChatRoomDTO> findAllRoom() {  // 채팅방 최근 순서로 반환
        List chatRoomDTOList = chatRoomRepository.findAll();
        Collections.reverse(chatRoomDTOList);
        return chatRoomDTOList;
    }*/

    // 특정 채팅방 정보만. 메시지는 아직 >> 같이?? 채팅방 위에 게시물 정보 같이 전달(채팅방 정보)
    /*public ChatRoomDTO selectRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        return roomToDTO(chatRoom);
    }*/



    // 메시지
    /*public ChatMessageDTO createMessage(ChatMessageDTO chatMessageDTO, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No room found for the provide id"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provide id"));
        ChatMessage newMessage = ChatMessage.createChatMessage(chatMessageDTO, chatRoom, user);
        chatMessageRepository.save(newMessage);
        return chatMessageDTO;
    }*/

    // 채팅방의 모든 메시지 가져오기
    /*public List<ChatMessageDTO> getAllMessages(Long roomId) { // 쿼리 작성 필요
        return chatMessageRepository.findAll().stream()
                .map(this::messageToDTO)
                .collect(Collectors.toList());
    }*/



    public ChatRoomDTO roomToDTO (LostChatRoom chatRoom) {
        String lastMessage = chatRoom.getChatMessages()
                .stream()
                .reduce((first, second) -> second) // 목록에서 마지막 요소 가져오기
                .map(ChatMessageLost::getMessage)
                .orElse(null);

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserTag(chatRoom.getHostUser().getTag())
                .guestUserTag(chatRoom.getGuestUser().getTag())
                .postId(chatRoom.getPostId())
                .lastMessage(lastMessage)
                .build();
    }
    public ChatRoomDTO roomToDTO (FoundChatRoom chatRoom) {
        String lastMessage = chatRoom.getChatMessages()
                .stream()
                .reduce((first, second) -> second) // 목록에서 마지막 요소 가져오기
                .map(ChatMessageFound::getMessage)
                .orElse(null);

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserTag(chatRoom.getHostUser().getTag())
                .guestUserTag(chatRoom.getGuestUser().getTag())
                .postId(chatRoom.getPostId())
                .lastMessage(lastMessage)
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