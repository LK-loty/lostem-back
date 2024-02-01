package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.KeywordDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column
    @NotNull
    @Size(max = 10)
    private String[] keyword;

    @Column
    private LocalDateTime time;



    public static Keyword createKeyword(KeywordDTO keywordDTO, User user) {
        return Keyword.builder()
                .user(user)
                .keyword(keywordDTO.getKeyword())
                .time(LocalDateTime.now())
                .build();
    }
}
