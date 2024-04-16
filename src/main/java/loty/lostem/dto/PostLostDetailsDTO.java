package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostLostDetailsDTO {
    private PostLostInfoDTO postLostDTO;
    private PostUserDTO postUserDTO;
}
