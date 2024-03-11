package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import loty.lostem.dto.*;
import loty.lostem.entity.*;
import loty.lostem.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

        //Optional<? extends ChatRoom> existingRoom = roomRepository.findByHostUserAndGuestUserAndPostLost(host, guest, (PostLost) post);
        //ChatRoom chatRoom = roomRepository.findByPostIdAndGuestUserTag(messageDTO.getPostId(), guest.getTag());

        if (messageDTO.getPostType().equals("lost")) {
            log.info("lost 게시글");
            PostLost post = lostRepository.findById(messageDTO.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("No post"));
            User host = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("No host"));

            ChatRoom chatRoom = roomRepository.findByPostTypeAndPostIdAndGuestUserTag("lost", messageDTO.getPostId(), guest.getTag());

            if (chatRoom == null) {
                log.info("채팅방을 생성합니다");
                //LostChatRoom lostChatRoom = new LostChatRoom(post.getPostId(), host.getTag(), guest.getTag());
                //chatRoom = new LostChatRoom(host.getTag(), guest.getTag(), post.getPostId());
                //chatRoom = LostChatRoom.createChatRoom(post.getPostId(), host.getTag(), guest.getTag());

                chatRoom = ChatRoom.builder()
                        .postType("lost")
                        .postId(post.getPostId())
                        .hostUserTag(host.getTag())
                        .guestUserTag(guest.getTag())
                        .build();
                roomRepository.save(chatRoom); // user 마다 채팅방 리스트화하면 저장할 때 힘듦+검색은 비교적 편함? 지금 로직은 생성할 때 바로 저장+검색은 오래 걸릴수도?

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
    /*public ChatRoomListDTO getAllRooms(Long userId) {
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
    }*/

    // 특정 채팅방 정보만. 메시지는 아직 >> 같이?? 채팅방 위에 게시물 정보 같이 전달(채팅방 정보)
    /*public ChatRoomDTO selectLostRoom(Long roomId, Long userId) {
        LostChatRoom chatRoom = roomLostRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));

        ChatRoomDTO chatRoomDTO = roomToDTO(chatRoom);

        // user id 들 tag 추가해서 보내는 작업
        User host = userRepository.findById(chatRoom.getHostUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        User guest = userRepository.findById(chatRoom.getGuestUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        chatRoomDTO.setTags(host.getTag(), guest.getTag());

        // 상대방 구분해서 프로필, 이미지, 태그 추가
        if (userId.equals(host.getUserId())) {
            String profile = guest.getProfile();
            String nickname = guest.getNickname();
            String tag = guest.getTag();

            ChatUserInfoDTO userInfoDTO = ChatUserInfoDTO.builder()
                    .profile(profile)
                    .nickname(nickname)
                    .tag(tag)
                    .build(); //.setCounterpart(profile, nickname, tag);

            chatRoomDTO.setCounterpart(userInfoDTO);
        } else if (userId.equals(guest.getUserId())) {
            String profile = host.getProfile();
            String nickname = host.getNickname();
            String tag = host.getTag();

            ChatUserInfoDTO userInfoDTO = ChatUserInfoDTO.builder()
                    .profile(profile)
                    .nickname(nickname)
                    .tag(tag)
                    .build();

            chatRoomDTO.setCounterpart(userInfoDTO);
        }

        // 게시물 정보 추가 .post dto에 불러오기만 하는 클래스(+user 불러오기만 하는 클래스) 만들어서 그걸 사용? . id, img, title, state
        PostLost post = lostRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No post"));

        ChatPostInfoDTO postInfoDTO = ChatPostInfoDTO.builder()
                .postId(post.getPostId())
                .image(post.getImages())
                .title(post.getTitle())
                .state(post.getState())
                .build();

        chatRoomDTO.setPostData(postInfoDTO);
        // 이전 대화 목록 추가

        return chatRoomDTO;
    }

    public ChatRoomDTO selectFoundRoom(Long roomId, Long userId) {
        FoundChatRoom chatRoom = roomFoundRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("No data"));

        ChatRoomDTO chatRoomDTO = roomToDTO(chatRoom);

        User host = userRepository.findById(chatRoom.getHostUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        User guest = userRepository.findById(chatRoom.getGuestUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data"));
        chatRoomDTO.setTags(host.getTag(), guest.getTag());

        if (userId.equals(host.getUserId())) {
            String profile = guest.getProfile();
            String nickname = guest.getNickname();
            String tag = guest.getTag();

            ChatUserInfoDTO userInfoDTO = ChatUserInfoDTO.builder()
                    .profile(profile)
                    .nickname(nickname)
                    .tag(tag)
                    .build();

            chatRoomDTO.setCounterpart(userInfoDTO);
        } else if (userId.equals(guest.getUserId())) {
            String profile = host.getProfile();
            String nickname = host.getNickname();
            String tag = host.getTag();

            ChatUserInfoDTO userInfoDTO = ChatUserInfoDTO.builder()
                    .profile(profile)
                    .nickname(nickname)
                    .tag(tag)
                    .build();
            chatRoomDTO.setCounterpart(userInfoDTO);
        }

        PostFound post = foundRepository.findById(chatRoom.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No post"));

        ChatPostInfoDTO postInfoDTO = ChatPostInfoDTO.builder()
                .postId(post.getPostId())
                .image(post.getImages())
                .title(post.getTitle())
                .state(post.getState())
                .build();

        chatRoomDTO.setPostData(postInfoDTO);
        // 이전 대화 목록 추가

        return chatRoomDTO;
    }*/



    // 메시지
    /*@Transactional
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
    }*/



    // 채팅방의 모든 메시지 가져오기. dto 재사용이라 바꿀 필요 있음
    /*public List<ChatMessageInfoDTO> getAllLostMessages(Long roomId) { // 쿼리 작성 필요
        return messageLostRepository.findByLostChatRoom_RoomId(roomId).stream()
                .map(this::messageToDTO)
                .collect(Collectors.toList());
    }
    public List<ChatMessageInfoDTO> getAllFoundMessages(Long roomId) {
        return messageFoundRepository.findByFoundChatRoom_RoomId(roomId).stream()
                .map(this::messageToDTO)
                .collect(Collectors.toList());
    }*/



    /*public ChatRoomDTO roomToDTO (LostChatRoom chatRoom) {
        String lastMessage = null;
        LocalDateTime time = null;
        ChatMessageInfoDTO messageInfoDTO;

        if (chatRoom.getChatMessages() != null) {
            *//*messageInfoDTO = ChatMessageInfoDTO.builder()
                    .message(chatRoom.getLastMessage())
                    .time(chatRoom.getLastMessageTime())
                    .build();*//*
            lastMessage = chatRoom.getLastMessage();
            time = chatRoom.getLastMessageTime();
        }

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserId(chatRoom.getHostUserId())
                .guestUserId(chatRoom.getGuestUserId())
                .postType(chatRoom.getType())
                //.messageInfoDTO()
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
                .postType(chatRoom.getType())
                .lastMessage(lastMessage)
                .time(time)
                .build();
    }*/

    public ChatRoomDTO roomToDTO (ChatRoom chatRoom) {
        String lastMessage = null;
        LocalDateTime time = null;

        if (chatRoom.getChatMessages() != null) {
            lastMessage = chatRoom.getLastMessage();
            time = chatRoom.getLastMessageTime();
        }

        return ChatRoomDTO.builder()
                .roomId(chatRoom.getRoomId())
                .hostUserTag(chatRoom.getHostUserTag())
                .guestUserTag(chatRoom.getGuestUserTag())
                //.postType(chatRoom.)
                .lastMessage(lastMessage)
                .time(time)
                .build();
    }

    /*public ChatMessageDTO messageToDTO(ChatMessageLost message) {
        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .senderTag(message.getSender().getTag())
                .postType("Lost")
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }
    public ChatMessageDTO messageToDTO(ChatMessageFound message) {
        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .senderTag(message.getSender().getTag())
                .postType("Found")
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }*/
    /*public ChatMessageInfoDTO messageToDTO(ChatMessageLost message) {
        return ChatMessageInfoDTO.builder()
                .messageId(message.getMessageId())
                .sender(message.getSender().getTag())
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }
    public ChatMessageInfoDTO messageToDTO(ChatMessageFound message) {
        return ChatMessageInfoDTO.builder()
                .messageId(message.getMessageId())
                .sender(message.getSender().getTag())
                .message(message.getMessage())
                .time(message.getTime())
                .build();
    }*/
}