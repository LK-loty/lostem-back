package loty.lostem.repository;

import loty.lostem.entity.ChatMessageLost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageLostRepository extends JpaRepository<ChatMessageLost, Long> {
}
