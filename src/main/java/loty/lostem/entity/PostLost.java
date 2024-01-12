package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.PostLostDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostLost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column
    @NotNull
    @Size(max = 20)
    private String title;

    @Column
    private String images;

    @Column
    @NotNull
    @Size(max = 10)
    private String category;

    @Column
    private LocalDateTime start;

    @Column
    private LocalDateTime end;

    @Column
    @NotNull
    @Size(max = 20)
    private String area;

    @Column
    @Size(max = 100)
    private String place;

    @Column
    @NotNull
    @Size(max = 30)
    private String item;

    @Column
    @NotNull
    @Size(max = 500)
    private String contents;

    @Column
    @NotNull
    @Size(max = 5)
    private String state;

    @Column
    @NotNull
    @Max(2)
    private int report;

    @Column
    @NotNull
    private LocalDateTime time;



    @OneToMany(mappedBy = "postLost")
    private List<LostReport> lostReports = new ArrayList<>();



    public static PostLost createPost(PostLostDTO postLostDTO, User user) {
        return PostLost.builder()
                .user(user)
                .title(postLostDTO.getTitle())
                .images(postLostDTO.getImages())
                .start(postLostDTO.getStart())
                .end(postLostDTO.getEnd())
                .area(postLostDTO.getArea())
                .place(postLostDTO.getPlace())
                .item(postLostDTO.getItem())
                .contents(postLostDTO.getContents())
                .state(postLostDTO.getState())
                .report(0)
                .time(LocalDateTime.now())
                .category(postLostDTO.getCategory())
                .build();
    }

    public void updatePostFields(PostLostDTO postLostDTO) {
        this.title = postLostDTO.getTitle();
        this.images = postLostDTO.getImages();
        this.category = postLostDTO.getCategory();
        this.start = postLostDTO.getStart();
        this.end = postLostDTO.getEnd();
        this.area = postLostDTO.getArea();
        this.place = postLostDTO.getPlace();
        this.item = postLostDTO.getItem();
        this.contents = postLostDTO.getContents();
        this.state = postLostDTO.getState();
        this.time = LocalDateTime.now();
    }
}
