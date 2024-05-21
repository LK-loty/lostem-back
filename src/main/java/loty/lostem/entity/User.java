package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.UserDTO;
import loty.lostem.dto.UserUpdateDTO;
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

    @Column(columnDefinition = "DECIMAL(5,2)")
    @Max(5)
    @Min(0)
    private Double star;

    @Column
    private int starCount;

    @Column
    @Size(max = 4)
    private String tag;

    @Column
    @NotNull
    private int report;

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
    private List<Review> reviews = new ArrayList<>();

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
                .profile("https://lostem-upload.s3.amazonaws.com/userBasic.png")
                .star((double) 0)
                .starCount(0)
                .tag(userDTO.getTag())
                .report(0)
                .role(UserRole.USER)
                .build();
    }

    public static void updateUserFields(User user, UserUpdateDTO userDTO) {
        user.name = userDTO.getName();
        user.nickname = userDTO.getNickname();
        user.phone = userDTO.getPhone();
        user.email = userDTO.getEmail();

        if (userDTO.getProfile() == null || userDTO.getProfile().isEmpty()) {
            user.profile = "https://lostem-upload.s3.amazonaws.com/userBasic.png";
        } else {
            user.profile = userDTO.getProfile();
        }
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateImage(String url) {
        this.profile = url;
    }

    public void updateStar(float star) {
        this.starCount++;
        double newStar = (double) Math.round(star * 100) / 100;
        if (this.starCount != 0) {
            this.star = (this.star * (this.starCount - 1) + newStar) / this.starCount;
        } else {
            this.star = newStar;
        }
    }

    public void updateReport() {
        this.report++;
    }

    public String getRole() {
        return this.role.name();
    }

    public static void deleteUser(User user) {
        user.name = "알 수 없음";
        user.nickname = "(알 수 없음)";
        user.username = "";
        user.password = "";
        user.phone = "";
        user.email = "";
        user.profile = "";
        user.star = Double.valueOf(0);
        user.starCount = 0;
        user.tag = "";
    }
}
