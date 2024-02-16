package loty.lostem.repository;

import loty.lostem.entity.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomLostRepository extends JpaRepository<LostChatRoom, Long> {
}
