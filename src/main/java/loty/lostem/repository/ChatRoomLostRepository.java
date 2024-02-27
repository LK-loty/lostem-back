package loty.lostem.repository;

import loty.lostem.entity.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomLostRepository extends JpaRepository<LostChatRoom, Long> {
    //LostChatRoom findByPostLost_IdAndHostUser_UserIdAndGuestUser_UserId(Long postLostId, Long hostUserId, Long guestUserId);
    LostChatRoom findByChatRoom_HostUser_UserIdAndChatRoom_GuestUser_UserId(Long hostUserId, Long guestUserId);
}
