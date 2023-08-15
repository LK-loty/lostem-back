package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Chatting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatting_id;

    @Column
    private int report;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;



    @OneToMany(mappedBy = "chatting")
    private List<ChattingReport> chattingReports = new ArrayList<>();

    @OneToMany(mappedBy = "chatting")
    private List<ChattingMessage> chattingMessages = new ArrayList<>();
}
