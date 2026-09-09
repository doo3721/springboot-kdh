package net.likelion.bebc25.sns.security.principal;

import lombok.RequiredArgsConstructor;
import net.likelion.bebc25.sns.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public Member getMember() {
        return member;
    }

    public Long getId() {
        return this.member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 문자열을 SimpleGrantedAuthority 객체로 변환 (ROLE_ 접두사 필수)
        return List.of(new SimpleGrantedAuthority(member.getRole()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true: 만료 안 됨)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부 (true: 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부 (true: 만료 안 됨)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true: 활성화)
    }
}
