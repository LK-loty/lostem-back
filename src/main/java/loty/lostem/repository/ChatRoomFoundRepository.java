package loty.lostem.repository;

import loty.lostem.entity.FoundChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomFoundRepository extends JpaRepository<FoundChatRoom, Long> {
    //FoundChatRoom findByPostFound_IdAndHostUser_UserIdAndGuestUser_UserId(Long postFoundId, Long hostUserId, Long guestUserId);
    FoundChatRoom findByHostUser_UserIdAndGuestUser_UserId(Long hostUserId, Long guestUserId);
}