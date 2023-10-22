package loty.lostem.entity;

import jakarta.persistence.*;
import lombok.*;
import loty.lostem.dto.UserDTO;
import loty.lostem.security.UserRole;

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
    private int start_count;

    @Column
    private String tag;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;


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
                .start_count(userDTO.getStart_count())
                .tag(userDTO.getTag())
                .role(UserRole.USER)
                .build();
    }

    public static void updateUserFields(User user, UserDTO userDTO) {
        user.name = userDTO.getName();
        user.nickname = userDTO.getNickname();
        user.phone = userDTO.getPhone();
        user.profile = userDTO.getProfile();
    }

    public static void updatePassword(User user, UserDTO userDTO) {
        user.password = userDTO.getPassword();
    }

    public static void updateStar(User user, UserDTO userDTO) {
        user.start_count++;
        user.star = (( user.star + userDTO.getStar() ) / user.start_count);
    }

    public String getRole() {
        return this.getRole();
    }
}
