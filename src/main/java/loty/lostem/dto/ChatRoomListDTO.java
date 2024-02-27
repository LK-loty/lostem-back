package loty.lostem.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ChatRoomListDTO {
    private List<ChatRoomDTO> chatRoomLostList;
    private List<ChatRoomDTO> chatRoomFoundList;
}
