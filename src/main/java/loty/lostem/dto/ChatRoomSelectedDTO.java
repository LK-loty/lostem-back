package loty.lostem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomSelectedDTO {
    private ChatRoomInfoDTO roomInfoDTO;
    private ChatPostInfoDTO postInfoDTO;
    private ChatUserInfoDTO userInfoDTO;



    public void setCounterpart(ChatUserInfoDTO userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
    }

    public void setPostData(ChatPostInfoDTO postInfoDTO) {
        this.postInfoDTO = postInfoDTO;
    }
}
