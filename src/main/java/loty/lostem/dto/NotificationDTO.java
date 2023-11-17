package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class NotificationDTO {
    private Long notificationId;
    private String notification;

    private LocalDateTime time;
}
