package loty.lostem.repository;

import loty.lostem.entity.ChatMessageFound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageFoundRepository extends JpaRepository<ChatMessageFound, Long> {
    List<ChatMessageFound> findByFoundChatRoom_RoomId(Long roomId);
}
