package com.hachic.webi.auth.api;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.auth.application.AuthTokenService;
import com.hachic.webi.auth.application.OauthService;
import com.hachic.webi.auth.application.StateService;
import com.hachic.webi.auth.dao.UserRepository;
import com.hachic.webi.auth.domain.SocialLoginType;
import com.hachic.webi.auth.domain.User;
import com.hachic.webi.auth.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@Tag(name = "OAuth", description = "소셜 로그인 인증을 시작하고 콜백을 처리하는 API")
public class OauthController {

	private final OauthService oauthService;
	private final UserRepository userRepository;
	private final AuthTokenService authTokenService;
	private final StateService stateService;
	@Value("${auth.jwt.access-ttl-sec}") private Duration accessTtlSec;
	@Value("${auth.jwt.refresh-ttl-sec}") private Duration refreshTtlSec;
	@Value("${frontend.redirect-url:http://localhost:3000}")
	private String frontendRedirectUrl;

	@Operation(
			summary = "소셜 로그인 프로세스 시작",
			description = "사용자를 소셜 로그인 페이지로 리다이렉트하여 인증 절차를 시작함",
			operationId = "startSocialLogin"
	)
	@GetMapping("/{socialLoginType}")
	public ResponseEntity<String> startSocialLogin(
			@PathVariable SocialLoginType socialLoginType,
			HttpServletRequest request) {
		log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
		String state = stateService.issue();
		request.getSession(true).setAttribute("oauth_state", state);
		String redirectUrl = oauthService.request(socialLoginType, state);

		return ResponseEntity.ok(redirectUrl);
	}

	@Operation(
			summary = "소셜 로그인 콜백 처리 (백엔드에서 사용 X)",
			description = "사용자가 소셜 로그인 후 콜백 URL로 받은 코드를 통해 액세스 토큰을 요청",
			operationId = "handleSocialLoginCallback"
	)
	@GetMapping("/{socialLoginType}/callback")
	public ResponseEntity<?> callback(
			@PathVariable("socialLoginType") SocialLoginType socialLoginType,
			@RequestParam("code") String code,
			@RequestParam("state") String state,
			HttpServletRequest request) throws IOException {

		// state 검증
		String savedState = (String) request.getSession().getAttribute("oauth_state");
		if (savedState == null || !savedState.equals(state)) { // 저장된 state가 없거나 앞에서 발급한 것과 다른 경우
			return ResponseEntity.badRequest().body("Invalid or expired state");
		}
		request.getSession().removeAttribute("oauth_state");

		UserResponse userResponse = oauthService.requestAccessTokenAndSaveUser(socialLoginType, code, state);
		if (userResponse == null) {
			return ResponseEntity.status(500).body("사용자 정보 저장 실패");
		}

		// 사용자 조회 실패 시 적절한 에러 응답
		User user = userRepository.findBySocialId(userResponse.socialId())
				.orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다: " + userResponse.socialId()));

		String accessJwtToken = authTokenService.createAccessToken(user);
		String refreshJwtToken = authTokenService.createRefreshToken(user);

		// 쿠키 설정
		ResponseCookie accessCookie = ResponseCookie.from("access_token", accessJwtToken)
				.httpOnly(true).secure(true).path("/")
				.sameSite("None").maxAge(accessTtlSec).build();
		ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshJwtToken)
				.httpOnly(true).secure(true).path("/")
				.sameSite("None").maxAge(refreshTtlSec).build();

		// 프론트엔드로 리다이렉트하면서 JWT 토큰을 쿼리 파라미터로 전달
		String redirectUrl = frontendRedirectUrl + "/auth/callback" + 
				"?success=true" +
				"&user=" + user.getName() +
				"&access_token=" + accessJwtToken +
				"&refresh_token=" + refreshJwtToken;

		return ResponseEntity.status(302)
				.header(HttpHeaders.SET_COOKIE, accessCookie.toString())
				.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
				.header(HttpHeaders.LOCATION, redirectUrl)
				.build();
	}
}
