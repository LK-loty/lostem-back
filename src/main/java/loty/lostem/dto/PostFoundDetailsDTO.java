package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostFoundDetailsDTO {
    private PostFoundDTO postFoundDTO;
    private PostUserDTO postUserDTO;
}
