package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.entity.RefreshToken;
import loty.lostem.entity.User;
import loty.lostem.repository.RefreshTokenRepository;
import loty.lostem.repository.UserRepository;
import loty.lostem.security.UserRole;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        String encoded = bCryptPasswordEncoder.encode(userDTO.getPassword());

        UserDTO.setPasswordEncode(userDTO, encoded);
        String tag = generateUniqueTag();
        userDTO.setTag(tag);

        User created = User.createUser(userDTO);
        userRepository.save(created);
        return userDTO;
    }

    @Transactional
    public String findUser(AStringDTO phone) {
        User user = userRepository.findByPhone(phone.getWord())
                .orElseThrow(() -> new IllegalArgumentException("No data for provided phone"));
        return user.getUsername();
    }

    @Transactional
    public LoginDTO resetPassword(LoginDTO loginDTO) {
        User selectedUser = userRepository.findByUsername(loginDTO.getUsername())
                        .orElseThrow(() -> new IllegalArgumentException("No data for provided username"));
        UserDTO userDTO = userToDTO(selectedUser);

        String encoded = bCryptPasswordEncoder.encode(loginDTO.getPassword());
        UserDTO.setPasswordEncode(userDTO, encoded);

        selectedUser.updateUserFields(selectedUser, userDTO);
        userRepository.save(selectedUser);

        return LoginDTO.builder().build();
    }

    public String checkUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            return username;
        } else {
            return null;
        }
    }

    public UserDetailDTO readUser(Long userId) { // 프로필 정보 확인 창
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        UserDetailDTO selectedDTO = detailToDTO(selectedUser);
        return selectedDTO;
    }

    public UserDTO loginUser(LoginDTO loginDTO) {
        User loginUser = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided username"));

        if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), loginUser.getPassword())) {
            UserDTO userDTO = userToDTO(loginUser);
            UserDTO.setPasswordNull(userDTO);
            return userDTO;
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
    }

    public UserPreviewDTO previewUser(String tag) {
        User loginUser = userRepository.findByTag(tag)
                .orElseThrow(() -> new IllegalArgumentException("No user data found for the provided token"));
        UserPreviewDTO dto = previewToDTO(loginUser);
        return dto;
    }

    @Transactional
    public UserDetailDTO updateUser(Long userId, UserDTO userDTO) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        if (selectedUser.getName().equals("알 수 없음")) {
            return null;
        }

        selectedUser.updateUserFields(selectedUser, userDTO);
        userRepository.save(selectedUser);
        UserDetailDTO changedDTO = detailToDTO(selectedUser);
        return changedDTO;
    }

    @Transactional
    public String deleteUser(Long userId, String password) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (bCryptPasswordEncoder.matches(password, selectedUser.getPassword())) {
            User.deleteUser(selectedUser);
            userRepository.save(selectedUser);

            return "OK";
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
    }


    public UserDTO userToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .password(user.getPassword())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profile(user.getProfile())
                .star(user.getStar())
                .starCount(user.getStarCount())
                .tag(user.getTag())
                .role(UserRole.valueOf(user.getRole()))
                .build();
    }

    public UserDetailDTO detailToDTO(User user) {
        return UserDetailDTO.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profile(user.getProfile())
                .star(user.getStar())
                .tag(user.getTag())
                .build();
    }

    public UserPreviewDTO previewToDTO(User user) {
        return UserPreviewDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .star(user.getStar())
                .tag(user.getTag())
                .build();
    }

    public static String generateTag() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder(4);

        for (int i = 0; i < 4 ; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }
        return codeBuilder.toString();
    }

    private String generateUniqueTag() {
        while (true) {
            String tag = generateTag();
            if (!userRepository.existsByTag(tag)) {
                return tag;
            }
        }
    }
}
