package loty.lostem.repository;

import loty.lostem.entity.FoundChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomFoundRepository extends JpaRepository<FoundChatRoom, Long> {
}
