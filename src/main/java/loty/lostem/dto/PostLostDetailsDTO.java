package loty.lostem.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostLostDetailsDTO {
    private PostLostDTO postLostDTO;
    private PostUserDTO postUserDTO;
}
