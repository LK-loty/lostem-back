package loty.lostem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class StarDTO {
    private String userTag;

    @Max(5)
    @Min(0)
    private float star;
}
