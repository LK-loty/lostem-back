package loty.lostem.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChatRoomInfoDTO {
    private Long roomId;
    private String hostUserTag;
    private String guestUserTag;

    private List<String> leaveUserTag;

    public void setTags(String hostUserTag, String guestUserTag) {
        this.hostUserTag = hostUserTag;
        this.guestUserTag = guestUserTag;
    }
}
