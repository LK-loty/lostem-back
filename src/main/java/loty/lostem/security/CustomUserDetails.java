package loty.lostem.security;

import lombok.RequiredArgsConstructor;
import loty.lostem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// spring security에 있는 userDetails 구현 클래스. 이 클래스를 통해 시큐리티가 사용자의 정보를 담아둠
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override // 계정 권한 담아두기 위해
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override // 계정 비밀번호 담아두기
    public String getPassword() {
        return user.getPassword();
    }

    @Override // 계정 아이디
    public String getUsername() {
        return user.getUsername();
    }

    @Override // 계정 만료 여부(true : 만료 안됨)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠김 여부(true: : 잠기지 않음)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override  // 계정 비밀번호 만료 여부(true : 만료 안됨)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 계정이 활성화 되었는지 여부(true : 활성화됨)
    public boolean isEnabled() {
        return true;
    }
}
