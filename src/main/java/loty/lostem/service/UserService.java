package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.UserDTO;
import loty.lostem.entity.User;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User created = User.createUser(userDTO);
        userRepository.save(created);
        UserDTO createdDTO = userToDTO(created);
        return createdDTO;
    }

    public UserDTO readUser(Long userId) { // 프로필 정보 확인 창
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        UserDTO selectedDTO = userToDTO(selectedUser);
        return selectedDTO;
    }

    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        selectedUser.updateUserFields(selectedUser, userDTO);
        userRepository.save(selectedUser);
        UserDTO changedDTO = userToDTO(selectedUser);
        return changedDTO;
    }

    @Transactional
    public UserDTO deleteUser(Long userId) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        UserDTO selectedDTO = userToDTO(selectedUser);
        userRepository.deleteById(userId);
        return selectedDTO;
    }



    public UserDTO userToDTO(User user) {
        return UserDTO.builder()
                .user_id(user.getUser_id())
                .name(user.getName())
                .nickname(user.getNickname())
                .id(user.getId())
                .password(user.getPassword())
                .phone(user.getPhone())
                .profile(user.getProfile())
                .star(user.getStar())
                .tag(user.getTag())
                .build();
    }
}
