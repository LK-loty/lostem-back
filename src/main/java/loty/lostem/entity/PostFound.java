package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.PostDTO;
import loty.lostem.dto.PostFoundDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostFound {
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
    @Size(max = 4)
    private String category;

    @Column
    @NotNull
    @Size(max = 20)
    private String period;

    @Column
    @NotNull
    @Size(max = 100)
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
    @Size(max = 10)
    private String type;

    @Column
    @Size(max = 50)
    private String storage;

    @Column
    @NotNull
    private LocalDateTime time;



    @OneToMany(mappedBy = "post")
    private List<PostReport> postReports = new ArrayList<>();



    public static Post createPost(PostDTO postDTO, User user) {
        return Post.builder()
                .user(user)
                .title(postDTO.getTitle())
                .images(postDTO.getImages())
                .type(postDTO.getType())
                .period(postDTO.getPeriod())
                .area(postDTO.getArea())
                .place(postDTO.getPlace())
                .item(postDTO.getItem())
                .contents(postDTO.getContents())
                .state(postDTO.getState())
                .report(0)
                .time(LocalDateTime.now())
                .category(postDTO.getCategory())
                .storage(postDTO.getStorage())
                .build();
    }

    public void updatePostFields(PostFoundDTO postDTO) {
        this.title = postDTO.getTitle();
        this.images = postDTO.getImages();
        this.category = postDTO.getCategory();
        this.period = postDTO.getPeriod();
        this.area = postDTO.getArea();
        this.place = postDTO.getPlace();
        this.item = postDTO.getItem();
        this.contents = postDTO.getContents();
        this.state = postDTO.getState();
        this.time = LocalDateTime.now();
        this.type = postDTO.getType();
        this.storage = postDTO.getStorage();
    }
}
