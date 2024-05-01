package loty.lostem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReviewDTO {
    @Max(5)
    @Min(0)
    private float star;

    private String postType;

    private Long postId;

    private String tag;

    @Size(max = 100)
    private String contents;
}
