package loty.lostem.repository;

import loty.lostem.entity.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomLostRepository extends JpaRepository<LostChatRoom, Long> {
    //LostChatRoom findByPostLost_IdAndHostUser_UserIdAndGuestUser_UserId(Long postLostId, Long hostUserId, Long guestUserId);
    LostChatRoom findByHostUser_UserIdAndGuestUser_UserId(Long hostUserId, Long guestUserId);
}
