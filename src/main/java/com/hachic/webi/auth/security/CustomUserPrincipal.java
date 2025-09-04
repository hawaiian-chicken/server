package com.hachic.webi.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hachic.webi.auth.domain.User;

public record CustomUserPrincipal(User user) implements UserDetails {

	public static CustomUserPrincipal from(User user) {
		return new CustomUserPrincipal(user);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한이 따로 없으면 기본 ROLE_USER 하나만
		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return null; // 소셜 로그인이라 패스워드 없음
	}

	@Override
	public String getUsername() {
		// User 엔티티의 식별자 -> social id
		return user.getSocialId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
