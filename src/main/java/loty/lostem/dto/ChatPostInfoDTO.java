package loty.lostem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatPostInfoDTO {
    private String postType;
    private Long postId;
    private String image;
    private String title;
    private String state;
    private String traderTag;

    public void setTraderTag(String traderTag) {
        this.traderTag = traderTag;
    }
}
