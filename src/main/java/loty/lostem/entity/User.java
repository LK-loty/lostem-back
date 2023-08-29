package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;
import loty.lostem.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    private String id;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String profile;

    @Column
    private float star;

    @Column
    private String tag;



    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Appraisal> appraisals = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Chatting> chattingList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();



    public static User createUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .id(userDTO.getId())
                .password(userDTO.getPassword())
                .phone(userDTO.getPhone())
                .profile(userDTO.getProfile())
                .star(userDTO.getStar())
                .tag(userDTO.getTag())
                .build();
    }
}
