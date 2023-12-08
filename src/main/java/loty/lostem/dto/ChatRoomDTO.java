package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomDTO {
    private Long roomId;
    private Long hostUserId;
    private Long guestUserId;
    private int report;
}
