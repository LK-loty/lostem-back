package loty.lostem.repository;

import loty.lostem.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    //List<ChatRoom> findByHostUser_UserId(Long userId);

    //ChatRoom findByPostIdAndGuestUserTag(Long postId, String guestUserTag);
    Optional<ChatRoom> findByPostTypeAndPostIdAndGuestUserTag(String postType, Long postId, String guestUserTag);
    /*@Query("SELECT cr FROM ChatRoom cr WHERE TYPE(cr) = LostChatRoom AND cr.postType = :postType AND cr.postId = :postId AND cr.guestUserTag = :guestUserTag")
    LostChatRoom findLostChatRoomByPostTypeAndPostIdAndGuestUserTag(@Param("postType") String postType, @Param("postId") Long postId, @Param("guestUserTag") String guestUserTag);

    @Query("SELECT cr FROM ChatRoom cr WHERE TYPE(cr) = FoundChatRoom AND cr.postType = :postType AND cr.postId = :postId AND cr.guestUserTag = :guestUserTag")
    FoundChatRoom findFoundChatRoomByPostTypeAndPostIdAndGuestUserTag(@Param("postType") String postType, @Param("postId") Long postId, @Param("guestUserTag") String guestUserTag);*/

    @Query("SELECT lr FROM ChatRoom lr WHERE lr.hostUserTag = :tag OR lr.guestUserTag = :tag")
    List<ChatRoom> findByHostUserTagOrGuestUserTag(@Param("tag") String tag);

    default List<ChatRoom> findByHostUserTagOrGuestUserTagAndNotInLeaveUser(String tag) {
        List<ChatRoom> chatRooms = findByHostUserTagOrGuestUserTag(tag);
        return chatRooms.stream()
                .filter(chatRoom -> !chatRoom.getLeaveUser().contains(tag))
                .collect(Collectors.toList());
    }

/*    @Query(value = "SELECT * FROM ChatRoom lr WHERE (lr.host_user_tag = :tag OR lr.guest_user_tag = :tag) AND :tag NOT IN (SELECT unnest(leave_user) FROM ChatRoom WHERE room_id = lr.room_id)", nativeQuery = true)
    List<ChatRoom> findByHostUserTagOrGuestUserTagAndNotInLeaveUser(@Param("tag") String tag);*/

    List<ChatRoom> findByPostTypeAndPostId(String postType, Long postId);
}