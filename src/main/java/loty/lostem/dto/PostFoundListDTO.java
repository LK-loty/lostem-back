package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostFoundListDTO {
    private Long postId;

    @NotNull
    @Size(max = 20)
    private String title;

    private String image;

    @NotNull
    @Size(max = 100)
    private String area;

    private LocalDateTime time;
}
