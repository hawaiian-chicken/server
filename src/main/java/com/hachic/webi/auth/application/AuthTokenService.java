package com.hachic.webi.auth.application;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hachic.webi.auth.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

	// TODO: 시크릿 키 숨기기
	private final String accessSecret = "BbQ5jczSshc0rXUO0aOs899/mV6+8mLA/rv5T16V5BA";
	private final String refreshSecret = "3iKQNeD2PbL84FJucEHWKUucISrHG9yiJTSZ+1gp+OE";
	@Value("${auth.jwt.access-ttl-sec}") private Duration accessTtlSec;
	@Value("${auth.jwt.refresh-ttl-sec}") private Duration refreshTtlSec;

	public String createAccessToken(User user) {
		return Jwts.builder()
				.subject(user.getId().toString())
				.claim("name", user.getName())
				.claim("provider", user.getProvider())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + accessTtlSec.toMillis()))
				.signWith(Keys.hmacShaKeyFor(accessSecret.getBytes()), Jwts.SIG.HS256)
				.compact();
	}

	public String createRefreshToken(User user) {
		return Jwts.builder()
				.subject(user.getId().toString())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + refreshTtlSec.toMillis()))
				.signWith(Keys.hmacShaKeyFor(refreshSecret.getBytes()), Jwts.SIG.HS256)
				.compact();
	}

	public Jws<Claims> parseAccessToken(String token) {
		return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(accessSecret.getBytes()))
				.build()
				.parseSignedClaims(token);
	}

	public Jws<Claims> parseRefreshToken(String token) {
		return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(refreshSecret.getBytes()))
				.build()
				.parseSignedClaims(token);
	}
}
