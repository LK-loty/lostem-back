package loty.lostem.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class KeywordListDTO {
    private List<PostLostListDTO> postLostDTO;
    private List<PostFoundListDTO> postFoundDTO;
}
