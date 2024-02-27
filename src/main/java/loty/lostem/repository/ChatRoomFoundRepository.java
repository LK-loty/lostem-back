package loty.lostem.repository;

import loty.lostem.entity.FoundChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomFoundRepository extends JpaRepository<FoundChatRoom, Long> {
    //FoundChatRoom findByPostFound_IdAndHostUser_UserIdAndGuestUser_UserId(Long postFoundId, Long hostUserId, Long guestUserId);
    FoundChatRoom findByHostUser_UserIdAndGuestUser_UserId(Long hostUserId, Long guestUserId);

    @Query("SELECT lr FROM FoundChatRoom lr WHERE lr.hostUser.userId = :userId OR lr.guestUser.userId = :userId")
    List<FoundChatRoom> findByHostUser_UserIdOrGuestUser_UserId(@Param("userId") Long userId);

    List<FoundChatRoom> findByGuestUser_UserId(Long userId);
}