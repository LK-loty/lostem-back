package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.PostDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Post {
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
    @Size(max = 20)
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

    public static void updatePostFields(Post post, PostDTO postDTO) { // 수정이 안 된 부분은?
        post.title = postDTO.getTitle();
        post.images = postDTO.getImages();
        post.category = postDTO.getCategory();
        post.period = postDTO.getPeriod();
        post.area = postDTO.getArea();
        post.place = postDTO.getPlace();
        post.item = postDTO.getItem();
        post.contents = postDTO.getContents();
        post.state = postDTO.getState();
        post.time = LocalDateTime.now();
        post.type = postDTO.getType();
        post.storage = postDTO.getStorage();
    }

    // 상태 변경 메소드, 신고 메소드 따로
}
