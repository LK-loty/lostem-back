package loty.lostem.repository;

import loty.lostem.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {
}
