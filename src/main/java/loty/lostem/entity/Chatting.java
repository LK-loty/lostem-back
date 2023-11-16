package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ChattingDTO;
import loty.lostem.dto.PostDTO;
import loty.lostem.dto.UserDTO;

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
    @NotNull
    @Size(max = 2)
    private int report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;



    @OneToMany(mappedBy = "chatting")
    private List<ChattingReport> chattingReports = new ArrayList<>();

    @OneToMany(mappedBy = "chatting")
    private List<ChattingMessage> chattingMessages = new ArrayList<>();
}
