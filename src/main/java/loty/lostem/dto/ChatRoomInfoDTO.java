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

    public void setTags(List<String> leaveUserTag) {
        this.leaveUserTag = leaveUserTag;
    }
}
