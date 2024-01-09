package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.UserDTO;
import loty.lostem.security.UserRole;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장
@Builder
@Getter
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    @NotNull
    @Size(min =2, max = 10)
    private String name;

    @Column
    @NotNull
    @Size(min =2, max = 10)
    private String nickname;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private String profile;

    @Column
    private float star;

    @Column
    private int starCount;

    @Column
    @Size(max = 4)
    private String tag;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;


    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLost> postLosts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostFound> postFounds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Appraisal> appraisals = new ArrayList<>();

    @OneToOne(mappedBy = "hostUser")
    private ChatRoom hostedChatRoom;

    @OneToOne(mappedBy = "guestUser")
    private ChatRoom joinedChatRoom;

    @OneToMany(mappedBy = "user")
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();



    public static User createUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .nickname(userDTO.getNickname())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .phone(userDTO.getPhone())
                .email(userDTO.getEmail())
                .profile(userDTO.getProfile())
                .star(userDTO.getStar())
                .starCount(userDTO.getStarCount())
                .tag(userDTO.getTag())
                .role(UserRole.USER)
                .build();
    }

    public static void updateUserFields(User user, UserDTO userDTO) {
        user.name = userDTO.getName();
        user.nickname = userDTO.getNickname();
        user.phone = userDTO.getPhone();
        user.email = userDTO.getEmail();
        user.profile = userDTO.getProfile();
    }

    /*public void modify(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }*/

    public static void updatePassword(User user, UserDTO userDTO) {
        user.password = userDTO.getPassword();
    }

    public static void updateStar(User user, UserDTO userDTO) {
        user.starCount++;
        user.star = (( user.star + userDTO.getStar() ) / user.starCount);
    }

    public String getRole() {
        return this.role.name();
    }

    public static void deleteUser(User user) {
        user.name = "알 수 없음";
        user.nickname = "알 수 없음";
        user.username = "";
        user.password = "";
        user.phone = "";
        user.email = "";
        user.profile = "";
        user.star = 0;
        user.starCount = 0;
        user.tag = "";
    }
}
