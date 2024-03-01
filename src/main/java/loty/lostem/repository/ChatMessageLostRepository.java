package loty.lostem.repository;

import loty.lostem.entity.ChatMessageLost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageLostRepository extends JpaRepository<ChatMessageLost, Long> {
    List<ChatMessageLost> findByLostChatRoom_RoomId(Long roomId);
}
