package com.team.todaycheck.main.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.todaycheck.main.entity.RefreshToken;
import com.team.todaycheck.main.entity.Token;
import com.team.todaycheck.main.exception.FalsifyTokenException;
import com.team.todaycheck.main.repository.RefreshTokenRepository;
import com.team.todaycheck.main.security.JwtTokenProvider;

@Service
public class JwtService {
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void login(Token tokenDto) {

		RefreshToken refreshToken = RefreshToken.builder().keyEmail(tokenDto.getKey())
				.refreshToken(tokenDto.getRefreshToken()).build();
		String loginUserEmail = refreshToken.getKeyEmail();

		RefreshToken token = refreshTokenRepository.existsByKeyEmail(loginUserEmail);
		if (token != null) { // 기존 존재하는 토큰 제거
			refreshTokenRepository.deleteByKeyEmail(loginUserEmail);
		}
		refreshTokenRepository.save(refreshToken);

	}

	public Optional<RefreshToken> getRefreshToken(String refreshToken) {

		return refreshTokenRepository.findByRefreshToken(refreshToken);
	}

	public Map<String, String> validateRefreshToken(String refreshToken) {
		try {
			RefreshToken refreshToken1 = getRefreshToken(refreshToken).get();
			String createdAccessToken = jwtTokenProvider.validateRefreshToken(refreshToken1);
			
			return createRefreshJson(createdAccessToken);
		} catch (NoSuchElementException e) {
			throw new FalsifyTokenException("변조되거나, 알 수 없는 RefreshToken 입니다.");
		}
	}

	public Map<String, String> createRefreshJson(String createdAccessToken) {
		Map<String, String> map = new HashMap<>();
		if (createdAccessToken == null) {
			map.put("code", "-1");
			map.put("message", "Refresh 토큰이 만료되었습니다. 로그인이 필요합니다.");
			map.put("accessToken", "null");
			return map;
		}
		// 기존에 존재하는 accessToken 제거
		map.put("code", "1");
		map.put("message", "Refresh 토큰을 통한 Access Token 생성이 완료되었습니다.");
		map.put("accessToken", createdAccessToken);

		return map;
	}

	public JwtService() {

	}
}