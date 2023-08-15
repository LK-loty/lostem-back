package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class NotificationDTO {
    private Long notification_id;
    private String notification;

    private LocalDateTime time;
}
