package loty.lostem.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomListDTO {
    private Long roomId;
    private ChatUserInfoDTO chatUserDTO;
    private ChatLastMessageDTO chatMessageDTO;
}
