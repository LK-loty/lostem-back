package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class NotificationDTO {
    private Long notificationId;

    @NotNull
    @Size(max = 20)
    private String notification;  // 알림 내용

    private LocalDateTime time;
}
