package com.hachic.webi.oauth.api;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hachic.webi.oauth.application.OauthService;
import com.hachic.webi.oauth.dto.UserInfo;
import com.hachic.webi.oauth.helper.constants.SocialLoginType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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

	@Operation(
			summary = "소셜 로그인 프로세스 시작",
			description = "사용자를 소셜 로그인 페이지로 리다이렉트하여 인증 절차를 시작함",
			operationId = "startSocialLogin"
	)
	@GetMapping("/{socialLoginType}")
	public ResponseEntity<String> socialLoginType(
			@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
		log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
		String redirectUrl = oauthService.request(socialLoginType);

		return ResponseEntity.ok(redirectUrl);
	}

	@Operation(
			summary = "소셜 로그인 콜백 처리",
			description = "사용자가 소셜 로그인 후 콜백 URL로 받은 코드를 통해 액세스 토큰을 요청",
			operationId = "handleSocialLoginCallback"
	)
	@GetMapping("/{socialLoginType}/callback")
	public void callback(
			@PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
			@RequestParam(name = "code") String code,
			HttpServletResponse response) throws IOException {
		log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

		// 액세스 토큰을 받아온 후 사용자 정보를 DB에 저장
		UserInfo user = oauthService.requestAccessTokenAndSaveUser(socialLoginType, code);

		String script = String.format("""
				<script>
					localStorage.setItem("accessToken", "%s");
					localStorage.setItem("name", "%s");
					localStorage.setItem("provider", "%s");
					window.location.href = "/login-success";
				</script>
				""", user.accessToken(), user.name(), user.provider());
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(script);
	}
}
