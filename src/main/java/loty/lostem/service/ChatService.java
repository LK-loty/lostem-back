package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.*;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository roomRepository;
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;

    // 채팅
    @Transactional
    public ChatRoomDTO createRoom(ChatMessageDTO messageDTO, Long userId) {
        User guest = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No guest"));
        ChatRoomDTO chatRoomDTO = null;

        if (messageDTO.getPostType().equals("lost")) {
            log.info("lost 게시글");
            PostLost post = lostRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            ChatRoom chatRoom = roomRepository.findByPostTypeAndPostIdAndGuestUserTag("lost", messageDTO.getPostId(), guest.getTag());

            if (chatRoom == null) {
                log.info("채팅방을 생성합니다");

                chatRoom = ChatRoom.builder()
                        .postType("lost")
                        .postId(post.getPostId())
                        .hostUserTag(host.getTag())
                        .guestUserTag(guest.getTag())
                        .build();
                roomRepository.save(chatRoom);

                ChatMessage chatMessage = ChatMessage.createChatMessage(messageDTO, chatRoom, guest.getTag());
                messageRepository.save(chatMessage);

                chatRoomDTO = roomToDTO(chatRoom);
                return chatRoomDTO;
            }
            return roomToDTO(chatRoom);
        }
        else if (messageDTO.getPostType().equals("found")){
            log.info("found 게시물");
            PostFound post = foundRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            ChatRoom chatRoom = roomRepository.findByPostTypeAndPostIdAndGuestUserTag("found", messageDTO.getPostId(), guest.getTag());

            if (chatRoom == null) {
                log.info("채팅방을 생성합니다.");
                chatRoom = ChatRoom.builder()
                        .postType("found")
                        .postId(post.getPostId())
                        .hostUserTag(host.getTag())
                        .guestUserTag(guest.getTag())
                        .build();
                roomRepository.save(chatRoom);

                ChatMessage chatMessage = ChatMessage.createChatMessage(messageDTO, chatRoom, guest.getTag());
                messageRepository.save(chatMessage);
            }
            return roomToDTO(chatRoom);
        }
        return null;
    }

    // 채탕방 목록 정보
    public List<ChatRoomListDTO> getAllRooms(Long userId) { // 상대방 + 메시지
        Optional<User> userOptional = userRepository.findById(userId);

        String userTag;
        List<ChatRoom> chatRooms = new ArrayList<>();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userTag = user.getTag();

            chatRooms = roomRepository.findByHostUserTagOrGuestUserTag(userTag);
        }

        List<ChatRoomListDTO> chatRoomListDTOS = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            //ChatRoomDTO chatRoomDTO = roomToDTO(chatRoom);
            Long roomId = chatRoom.getRoomId();

            User host = userRepository.findByTag(chatRoom.getHostUserTag())
                    .orElseThrow(() -> new IllegalArgumentException("No data"));
            User guest = userRepository.findByTag(chatRoom.getGuestUserTag())
                    .orElseThrow(() -> new IllegalArgumentException("No data"));
            ChatUserInfoDTO userInfoDTO = null;
            if (userId.equals(host.getUserId())) {
                String profile = guest.getProfile();
                String nickname = guest.getNickname();
                String tag = guest.getTag();

                userInfoDTO = ChatUserInfoDTO.builder()
                        .profile(profile)
                        .nickname(nickname)
                        .tag(tag)
                        .build();
            } else if (userId.equals(guest.getUserId())) {
                String profile = host.getProfile();
                String nickname = host.getNickname();
                String tag = host.getTag();

                userInfoDTO = ChatUserInfoDTO.builder()
                        .profile(profile)
                        .nickname(nickname)
                        .tag(tag)
                        .build();
            }

            ChatMessage lastMessage = messageRepository.findByChatRoom_RoomId(chatRoom.getRoomId())
                    .stream().reduce((first, second) -> second).orElse(null);
            ChatLastMessageDTO messageDTO = lastMsgToDTO(lastMessage);

            ChatRoomListDTO chatRoomListDTO = ChatRoomListDTO.builder()
                    .roomId(roomId)
                    .chatUserDTO(userInfoDTO)
                    .chatMessageDTO(messageDTO)
                    .build();
            chatRoomListDTOS.add(chatRoomListDTO);
        }

        return chatRoomListDTOS;
    }

    // 글쓴이 != 본인
    public Long getRoomIdByPost(String postType, Long postId, Long userId) {
        User guest = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));

        if (postType.equals("lost")) {
            log.info("lost 타입 확인");
            PostLost post = lostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("no post"));

            if (!post.getUser().getUserId().equals(userId)) {
                log.info("글쓴이가 본인이 아니므로 room ID를 반환합니다.");
                ChatRoom chatRoom = roomRepository.findByPostTypeAndPostIdAndGuestUserTag(postType, postId, guest.getTag());
                return chatRoom.getRoomId();
            }
        } else if (postType.equals("found")) {
            log.info("found 타입 확인");
            PostFound post = foundRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("no post"));

            if (!post.getUser().getUserId().equals(userId)) {
                log.info("글쓴이가 본인이 아니므로 room ID를 반환합니다.");
                ChatRoom chatRoom = roomRepository.findByPostTypeAndPostIdAndGuestUserTag(postType, postId, guest.getTag());
                return chatRoom.getRoomId();
            }
        }
        return null;
    }

    // 글쓴이 = 본인
    public List<ChatRoomListDTO> getRoomListByPost(String postType, Long postId, Long userId) {
        List<ChatRoomListDTO> chatRoomListDTOS = new ArrayList<>();

        if (postType.equals("lost")) {
            log.info("lost 타입 확인");
            PostLost post = lostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("no post"));

            if (post.getUser().getUserId().equals(userId)) {
                log.info("글쓴이가 본인이므로 room IDs를 반환합니다.");
                List<ChatRoom> chatRooms = roomRepository.findByPostTypeAndPostId("lost", post.getPostId());
                for (ChatRoom chatRoom : chatRooms) {
                    Long roomId = chatRoom.getRoomId();

                    User guest = userRepository.findByTag(chatRoom.getGuestUserTag())
                            .orElseThrow(() -> new IllegalArgumentException("No data"));
                    ChatUserInfoDTO userInfoDTO = null;

                    String profile = guest.getProfile();
                    String nickname = guest.getNickname();
                    String tag = guest.getTag();

                    userInfoDTO = ChatUserInfoDTO.builder()
                            .profile(profile)
                            .nickname(nickname)
                            .tag(tag)
                            .build();

                    ChatMessage lastMessage = messageRepository.findByChatRoom_RoomId(chatRoom.getRoomId())
                            .stream().reduce((first, second) -> second).orElse(null);
                    ChatLastMessageDTO messageDTO = lastMsgToDTO(lastMessage);

                    ChatRoomListDTO chatRoomListDTO = ChatRoomListDTO.builder()
                            .roomId(roomId)
                            .chatUserDTO(userInfoDTO)
                            .chatMessageDTO(messageDTO)
                            .build();
                    chatRoomListDTOS.add(chatRoomListDTO);
                }
                return chatRoomListDTOS;
            }
        } else if (postType.equals("found")) {
            log.info("found 타입 확인");
            PostFound post = foundRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("no post"));

            if (post.getUser().getUserId().equals(userId)) {
                log.info("글쓴이가 본인이므로 room IDs를 반환합니다.");
                List<ChatRoom> chatRooms = roomRepository.findByPostTypeAndPostId("found", post.getPostId());
                for (ChatRoom chatRoom : chatRooms) {
                    Long roomId = chatRoom.getRoomId();

                    User guest = userRepository.findByTag(chatRoom.getGuestUserTag())
                            .orElseThrow(() -> new IllegalArgumentException("No data"));
                    ChatUserInfoDTO userInfoDTO = null;

                    String profile = guest.getProfile();
                    String nickname = guest.getNickname();
                    String tag = guest.getTag();

                    userInfoDTO = ChatUserInfoDTO.builder()
                            .profile(profile)
                            .nickname(nickname)
                            .tag(tag)
                            .build();

                    ChatMessage lastMessage = messageRepository.findByChatRoom_RoomId(chatRoom.getRoomId())
                            .stream().reduce((first, second) -> second).orElse(null);
                    ChatLastMessageDTO messageDTO = lastMsgToDTO(lastMessage);

                    ChatRoomListDTO chatRoomListDTO = ChatRoomListDTO.builder()
                            .roomId(roomId)
                            .chatUserDTO(userInfoDTO)
                            .chatMessageDTO(messageDTO)
                            .build();
                    chatRoomListDTOS.add(chatRoomListDTO);
                }
            }
            return chatRoomListDTOS;
        }
        return null;
    }

    // 특정 채팅방 정보만. 메시지는 아직 >> 같이?? 채팅방 위에 게시물 정보 같이 전달(채팅방 정보)
    public ChatRoomSelectedDTO selectRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));

        User host = userRepository.findByTag(chatRoom.getHostUserTag())
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        User guest = userRepository.findByTag(chatRoom.getGuestUserTag())
                .orElseThrow(() -> new IllegalArgumentException("No data"));

        ChatRoomInfoDTO roomInfoDTO = ChatRoomInfoDTO.builder()
                .hostUserTag(host.getTag())
                .guestUserTag(guest.getTag())
                .build();

        ChatRoomSelectedDTO selectedDTO = ChatRoomSelectedDTO.builder()
                .roomInfoDTO(roomInfoDTO)
                .build();

        // 상대방 구분해서 프로필, 이미지, 태그 추가
        if (userId.equals(host.getUserId())) {
            String profile = guest.getProfile();
            String nickname = guest.getNickname();
            String tag = guest.getTag();

            ChatUserInfoDTO userInfoDTO = ChatUserInfoDTO.builder()
                    .profile(profile)
                    .nickname(nickname)
                    .tag(tag)
                    .build();
            selectedDTO.setCounterpart(userInfoDTO);
        } else if (userId.equals(guest.getUserId())) {
            String profile = host.getProfile();
            String nickname = host.getNickname();
            String tag = host.getTag();

            ChatUserInfoDTO userInfoDTO = ChatUserInfoDTO.builder()
                    .profile(profile)
                    .nickname(nickname)
                    .tag(tag)
                    .build();
            selectedDTO.setCounterpart(userInfoDTO);
        }

        if (chatRoom.getPostType().equals("lost")) {
            // 게시물 정보 추가 .post dto에 불러오기만 하는 클래스(+user 불러오기만 하는 클래스) 만들어서 그걸 사용? . id, img, title, state
            PostLost post = lostRepository.findById(chatRoom.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));

            ChatPostInfoDTO postInfoDTO = ChatPostInfoDTO.builder()
                    .postType("lost")
                    .postId(post.getPostId())
                    .image(post.getImages())
                    .title(post.getTitle())
                    .state(post.getState())
                    .build();
            selectedDTO.setPostData(postInfoDTO);
        } else if (chatRoom.getPostType().equals("found")) {
            PostFound post = foundRepository.findById(chatRoom.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));

            ChatPostInfoDTO postInfoDTO = ChatPostInfoDTO.builder()
                    .postType("found")
                    .postId(post.getPostId())
                    .image(post.getImages())
                    .title(post.getTitle())
                    .state(post.getState())
                    .build();
            selectedDTO.setPostData(postInfoDTO);
        }
        // 이전 대화 목록 추가

        return selectedDTO;
    }

    public ChatMessageDTO socketRoom(ChatRoomIdDTO roomIdDTO) {
        ChatRoom chatRoom = roomRepository.findById(roomIdDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No data"));

        User host = userRepository.findByTag(chatRoom.getHostUserTag())
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        User guest = userRepository.findByTag(chatRoom.getGuestUserTag())
                .orElseThrow(() -> new IllegalArgumentException("No data"));

        return ChatMessageDTO.builder()
                .roomId(chatRoom.getRoomId())
                .senderTag(guest.getTag())
                .receiverTag(host.getTag())
                .build();
    }



    // 메시지
    @Transactional
    public ChatMessageDTO createMessage(ChatMessageDTO chatMessageDTO, Long userId) {
        ChatRoom chatRoom = roomRepository.findById(chatMessageDTO.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("No room found for the provide id"));
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));
        User receiver = null;

        if (sender.getTag().equals(chatRoom.getHostUserTag())) {
            receiver = userRepository.findByTag(chatRoom.getGuestUserTag())
                    .orElseThrow(() -> new IllegalArgumentException("No receiver"));
        } else if (sender.getTag().equals(chatRoom.getGuestUserTag())) {
            receiver = userRepository.findByTag(chatRoom.getHostUserTag())
                    .orElseThrow(() -> new IllegalArgumentException("No receiver"));
        }

        ChatMessage newMessage = ChatMessage.createChatMessage(chatMessageDTO, chatRoom, sender.getTag());
        newMessage = messageRepository.save(newMessage);

        ChatMessageDTO messageDTO = messageToDTO(newMessage);
        messageDTO.setReceiverTag(receiver.getTag());

        return messageDTO;
    }



    // 채팅방의 모든 메시지 가져오기. dto 재사용이라 바꿀 필요 있음
    public List<ChatMessageInfoDTO> getAllMessages(Long roomId) { // 쿼리 작성 필요
        return messageRepository.findByChatRoom_RoomId(roomId).stream()
                .map(this::InfoMsgToDTO)
                .collect(Collectors.toList());
    }



    public ChatRoomDTO roomToDTO (ChatRoom chatRoom) {
        String lastMessage = null;
        LocalDateTime time = null;
        ChatLastMessageDTO messageInfoDTO;

        if (chatRoom.getChatMessages() != null) {
            messageInfoDTO = ChatLastMessageDTO.builder()
                    .message(chatRoom.getLastMessage())
                    .time(chatRoom.getLastMessageTime())
                    .build();
            lastMessage = chatRoom.getLastMessage();
            time = chatRoom.getLastMessageTime();
        }

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserTag(chatRoom.getHostUserTag())
                .guestUserTag(chatRoom.getGuestUserTag())
                .postType(chatRoom.getPostType())
                .build();
    }

    public ChatMessageDTO messageToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .roomId(message.getChatRoom().getRoomId())
                .senderTag(message.getSender())
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }

    public ChatLastMessageDTO lastMsgToDTO(ChatMessage message) {
        return ChatLastMessageDTO.builder()
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }

    public ChatMessageInfoDTO InfoMsgToDTO(ChatMessage message) {
        return ChatMessageInfoDTO.builder()
                .senderTag(message.getSender())
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }
}