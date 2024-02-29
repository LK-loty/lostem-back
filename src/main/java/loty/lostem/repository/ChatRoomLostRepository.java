package loty.lostem.repository;

import loty.lostem.entity.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomLostRepository extends JpaRepository<LostChatRoom, Long> {
    //LostChatRoom findByPostLost_IdAndHostUser_UserIdAndGuestUser_UserId(Long postLostId, Long hostUserId, Long guestUserId);
    //LostChatRoom findByHostUser_UserIdAndGuestUser_UserId(Long hostUserId, Long guestUserId);

    LostChatRoom findByPostIdAndGuestUserId(Long postId, Long guestUserId);

    @Query("SELECT lr FROM LostChatRoom lr WHERE lr.hostUserId = :userId OR lr.guestUserId = :userId")
    //List<LostChatRoom> findByHostUser_UserIdOrGuestUser_UserId(@Param("userId") Long userId);
    List<LostChatRoom> findByHostUserIdOrGuestUserId(@Param("userId") Long userId);

    //List<LostChatRoom> findByGuestUser_UserId(Long userId);
}
