package loty.lostem.repository;

import loty.lostem.entity.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomLostRepository extends JpaRepository<LostChatRoom, Long> {
    //LostChatRoom findByPostLost_IdAndHostUser_UserIdAndGuestUser_UserId(Long postLostId, Long hostUserId, Long guestUserId);
    LostChatRoom findByHostUser_UserIdAndGuestUser_UserId(Long hostUserId, Long guestUserId);

    @Query("SELECT lr FROM LostChatRoom lr WHERE lr.hostUser.userId = :userId OR lr.guestUser.userId = :userId")
    List<LostChatRoom> findByHostUser_UserIdOrGuestUser_UserId(@Param("userId") Long userId);
    List<LostChatRoom> findByGuestUser_UserId(Long userId);
}
