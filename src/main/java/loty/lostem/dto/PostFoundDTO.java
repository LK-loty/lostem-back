package loty.lostem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostFoundDTO {
    private Long postId;

    private Long userId;

    @NotNull
    @Size(max = 20)
    private String title;  // 제목

    private String images;  // 이미지 url

    @NotNull
    @Size(max = 4)
    private String category;  // 카테고리 : 물건 종류

    @NotNull
    private String period;  // 기간

    @NotNull
    @Size(max = 100)
    private String area;  // 분실 지역 (필수 입력 지역)

    @Size(max = 100)
    private String place;  // 분실 장소 (세부 장소)

    @NotNull
    @Size(max = 30)
    private String item;  // 분실물명

    @NotNull
    @Size(max = 500)
    private String contents;  // 내용

    @NotNull
    @Size(max = 5)
    private String state;  // 게시물 상태

    private int report;

    @Size(max = 50)
    private String storage;  // 보관 장소

    private LocalDateTime time;



    public void defaultImage(String url) {
        this.images = url;
    }
}
