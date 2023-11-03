package loty.lostem.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import loty.lostem.entity.User;
import loty.lostem.repository.UserRepository;
import loty.lostem.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

// userDetailsService 구현 클래스. 시큐리티가 요청 가로챌 때 username, password 변수 가로채는데 password 부분은 알아서 처리
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    // 로그인 시 db에서 유저정보와 권한정보 가져온 후 이를 기반으로 userDetails.User 객체 생성해서 리턴
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));

        Set<GrantedAuthority> grantedAuthority = new HashSet<>();
        //List<GrantedAuthority> authorities = new ArrayList<>();
        //authorities.add(new SimpleGrantedAuthority(user.getRoleKey()));
        grantedAuthority.add(new SimpleGrantedAuthority("USER"));
        return new CustomUserDetails(user);

        /*if (user != null) {
            grantedAuthority.add(new SimpleGrantedAuthority("USER"));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthority);
        } else {
            throw new UsernameNotFoundException("can not find User : " + username);
        }*/
    }
}
