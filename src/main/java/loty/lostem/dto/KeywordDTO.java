package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class KeywordDTO {
    private Long keywordId;

    @NotNull
    @Size(max = 10)
    private String keyword;  // 키워드 문자
}
