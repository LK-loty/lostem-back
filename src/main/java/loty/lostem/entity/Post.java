package loty.lostem.entity;

import jakarta.persistence.*;
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
    private static Post post;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    @NotNull
    @Size(max = 20)
    private String title;

    @Column
    private String image;

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
    private String field;

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
    private String explain;

    @Column
    @NotNull
    @Size(max = 5)
    private String state;

    @Column
    @NotNull
    @Size(max = 2)
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
                .image(postDTO.getImage())
                .type(postDTO.getType())
                .period(postDTO.getPeriod())
                .place(postDTO.getPlace())
                .item(postDTO.getItem())
                .explain(postDTO.getExplain())
                .state(postDTO.getStorage())
                .report(postDTO.getReport())
                .time(postDTO.getTime())
                .category(postDTO.getCategory())
                .storage(postDTO.getStorage())
                .build();
    }

    public static void updatePostFields(Post post, PostDTO postDTO) { // 수정이 안 된 부분은?
        post.title = postDTO.getTitle();
        post.image = postDTO.getImage();
        post.category = postDTO.getCategory();
        post.period = postDTO.getPeriod();
        post.field = postDTO.getField();
        post.place = postDTO.getPlace();
        post.item = postDTO.getItem();
        post.explain = postDTO.getExplain();
        post.state = postDTO.getState();
        post.time = LocalDateTime.now();
        post.type = postDTO.getType();
        post.storage = postDTO.getStorage();
    }

    // 상태 변경 메소드, 신고 메소드 따로
}
