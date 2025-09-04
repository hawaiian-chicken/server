package com.hachic.webi.auth.security;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hachic.webi.auth.application.AuthTokenService;
import com.hachic.webi.auth.dao.UserRepository;
import com.hachic.webi.auth.domain.User;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	private final AuthTokenService authTokenService;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = null;

		// Authorization 헤더 확인
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}

		// access_token 쿠키 확인
		if (token == null && request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("access_token")) {
					token = cookie.getValue();
					break;
				}
			}
		}

		if (token != null) {
			try {
				Claims claims = authTokenService.parseAccessToken(token).getPayload();
				String userId = claims.getSubject();
				User user = userRepository.findById(new ObjectId(userId)).orElse(null);

				if (user != null) {
					CustomUserPrincipal principal = CustomUserPrincipal.from(user);
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							principal, null, principal.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			} catch (Exception e) {
				log.warn("JWT 토큰 검증 실패: {}", e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}
}
