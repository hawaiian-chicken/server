package com.hachic.webi.auth.api;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.auth.application.AuthTokenService;
import com.hachic.webi.auth.dao.UserRepository;
import com.hachic.webi.auth.domain.User;
import com.hachic.webi.auth.dto.UserResponse;
import com.hachic.webi.auth.security.CustomUserPrincipal;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthQueryController {
	private final AuthTokenService authTokenService;
	private final UserRepository users;

	@GetMapping("/me")
	public ResponseEntity<?> me(@AuthenticationPrincipal CustomUserPrincipal principal) {
		if (principal == null) {
			return ResponseEntity.status(401)
					.body("{\"error\": \"인증이 필요합니다\", \"code\": \"UNAUTHORIZED\"}");
		}
		User user = principal.user();
		return ResponseEntity.ok(UserResponse.of(user.getName(), user.getSocialId(), user.getProvider()));
	}

	@PostMapping("/refresh")
	public ResponseEntity<Void> refresh(HttpServletRequest request) {
		String refreshToken = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refresh_token".equals(cookie.getName())) {
					refreshToken = cookie.getValue();
				}
			}
		}
		if (refreshToken == null) {
			return ResponseEntity.status(401).build();
		}

		Claims claims;
		try {
			claims = authTokenService.parseRefreshToken(refreshToken).getPayload();
		} catch (Exception e) {
			return ResponseEntity.status(401).build();
		}

		String userId = claims.getSubject();
		User user = users.findById(new ObjectId(userId)).orElse(null);
		if (user == null) {
			return ResponseEntity.status(401).build();
		}

		String newAccess = authTokenService.createAccessToken(user);
		ResponseCookie access = ResponseCookie.from("access_token", newAccess)
				.httpOnly(true).secure(true).path("/").sameSite("None").maxAge(900).build();

		return ResponseEntity.ok().header("Set-Cookie", access.toString()).build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		ResponseCookie killA = ResponseCookie.from("access_token", "")
				.httpOnly(true).secure(true).path("/").sameSite("None").maxAge(0).build();
		ResponseCookie killR = ResponseCookie.from("refresh_token", "")
				.httpOnly(true).secure(true).path("/").sameSite("None").maxAge(0).build();
		return ResponseEntity.ok()
				.header("Set-Cookie", killA.toString())
				.header("Set-Cookie", killR.toString())
				.build();
	}
}
