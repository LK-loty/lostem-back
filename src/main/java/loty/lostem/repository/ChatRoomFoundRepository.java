package loty.lostem.repository;

import loty.lostem.entity.FoundChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomFoundRepository extends JpaRepository<FoundChatRoom, Long> {
    //FoundChatRoom findByPostFound_IdAndHostUser_UserIdAndGuestUser_UserId(Long postFoundId, Long hostUserId, Long guestUserId);
    FoundChatRoom findByChatRoom_HostUser_UserIdAndChatRoom_GuestUser_UserId(Long hostUserId, Long guestUserId);
}