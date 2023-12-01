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
    @Size(max = 10)
    private String name;

    @Column
    @NotNull
    @Size(max = 20)
    private String nickname;

    @Column
    @NotNull
    @Size(max = 20)
    private String username; // id

    @Column
    @NotNull
    //@Size(max = 20) bCryptPasswordEncoder 사용하므로 필요 없음
    private String password;

    @Column
    @NotNull
    @Size(max = 11)
    private String phone;

    @Column
    @NotNull
    @Size(max = 30)
    private String email;

    @Column
    private String profile;

    @Column
    @NotNull
    @Max(2)
    private float star;

    @Column
    @NotNull
    @Max(4)
    private int starCount;

    @Column
    @Size(max = 4)
    private String tag;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;


    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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
}
