package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
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
    private LocalDateTime date;

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



    @OneToMany(mappedBy = "postFound")
    private List<FoundReport> foundReports = new ArrayList<>();



    public static PostFound createPost(PostFoundDTO postFoundDTO, User user) {
        return PostFound.builder()
                .user(user)
                .title(postFoundDTO.getTitle())
                .images(postFoundDTO.getImages())
                .date(postFoundDTO.getDate())
                .area(postFoundDTO.getArea())
                .place(postFoundDTO.getPlace())
                .item(postFoundDTO.getItem())
                .contents(postFoundDTO.getContents())
                .state(postFoundDTO.getState())
                .report(0)
                .time(LocalDateTime.now())
                .category(postFoundDTO.getCategory())
                .storage(postFoundDTO.getStorage())
                .build();
    }

    public void updatePostFields(PostFoundDTO postFoundDTO) {
        this.title = postFoundDTO.getTitle();
        this.images = postFoundDTO.getImages();
        this.category = postFoundDTO.getCategory();
        this.date = postFoundDTO.getDate();
        this.area = postFoundDTO.getArea();
        this.place = postFoundDTO.getPlace();
        this.item = postFoundDTO.getItem();
        this.contents = postFoundDTO.getContents();
        this.state = postFoundDTO.getState();
        this.time = LocalDateTime.now();
        this.storage = postFoundDTO.getStorage();
    }
}
