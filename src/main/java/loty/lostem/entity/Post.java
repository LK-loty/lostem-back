package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Long post_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column
    private String image;

    @Column
    private String type;

    @Column
    private String period;

    @Column
    private String place;

    @Column
    private String item;

    @Column
    private String explain;

    @Column
    private String state;

    @Column
    private String category;

    @Column
    private String storage;

    @Column
    private int report;

    @Column
    private LocalDateTime time;



    @OneToMany(mappedBy = "post")
    private List<PostReport> postReports = new ArrayList<>();
}
