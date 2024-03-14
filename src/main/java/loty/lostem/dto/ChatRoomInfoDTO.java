package loty.lostem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomInfoDTO {
    private Long roomId;
    private String hostUserTag;
    private String guestUserTag;

    public void setTags(String hostUserTag, String guestUserTag) {
        this.hostUserTag = hostUserTag;
        this.guestUserTag = guestUserTag;
    }
}
