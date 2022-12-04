package com.team.todaycheck.main.controller;

import java.util.Map;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.team.todaycheck.main.DTO.LoginRequestDTO;
import com.team.todaycheck.main.DTO.LoginResponseDTO;
import com.team.todaycheck.main.DTO.MessageDTO;
import com.team.todaycheck.main.DTO.RegistryDTO;
import com.team.todaycheck.main.entity.Token;
import com.team.todaycheck.main.exception.NotAuthorizationException;
import com.team.todaycheck.main.exception.InvalidateTokenException;
import com.team.todaycheck.main.oauth.CreateOAuthUser;
import com.team.todaycheck.main.service.JwtService;
import com.team.todaycheck.main.service.LoginService;

@RestController
public class LoginController {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private CreateOAuthUser createOauthUser;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public LoginResponseDTO findId(@RequestBody LoginRequestDTO loginDto , HttpServletResponse response) throws AccountNotFoundException {
		LoginResponseDTO result = loginService.findId(loginDto);
		Cookie cookie = new Cookie("refreshToken", result.getRefreshToken());
		cookie.setPath("/");
		cookie.setMaxAge(1209600);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return result;
	} 
	
	@RequestMapping(value="/googlelogin" , method = RequestMethod.POST)
	public LoginResponseDTO getGoogleOAuthUserInfo(@RequestParam("code") String code , HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
		Token result = null;
		result = createOauthUser.createGoogleUser(code);
		Cookie cookie = new Cookie("refreshToken", result.getRefreshToken());
		cookie.setPath("/");
		cookie.setMaxAge(1209600);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		
		return LoginResponseDTO.builder()
				.accessToken(result.getAccessToken())
				.refreshToken(result.getRefreshToken())
				.grantType(result.getGrantType())
				.key(result.getKey())
				.build();
	}
	
	@RequestMapping(value="/naverlogin" , method = RequestMethod.POST)
	public LoginResponseDTO getNaverOAuthUserInfo(@RequestParam("code") String code , HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
		Token result = null;
		result = createOauthUser.createNaverUser(code);
		Cookie cookie = new Cookie("refreshToken", result.getRefreshToken());
		cookie.setPath("/");
		cookie.setMaxAge(1209600);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		
		System.out.println(result.toString());
		
		return LoginResponseDTO.builder()
				.accessToken(result.getAccessToken())
				.refreshToken(result.getRefreshToken())
				.grantType(result.getGrantType())
				.key(result.getKey())
				.build();
	}
	
	/* */
	@RequestMapping(value = "/admin/test" , method = RequestMethod.GET)
	public String testAdminAuthorizaztion() {
		return "어드민 데이터";
	}
	
	@RequestMapping(value = "/user/test" , method = RequestMethod.GET)
	public String testUserAuthorizaztion() {
		return "유저 데이터 POST";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public MessageDTO registId(@RequestBody RegistryDTO data) throws AccountException {
		return loginService.createId(data);
	}

	@RequestMapping(value = "/accessDenied_page", method = RequestMethod.GET)
	public MessageDTO denialAccess() {
		return MessageDTO.builder()
				.code("-1")
				.message("인가되지 않은 요청")
				.build();
	}
	
	@RequestMapping(value = "/refresh" , method = RequestMethod.GET) // 
	public MessageDTO validateRefreshToken(@CookieValue(name = "refreshToken") String cookie) {
		if(cookie == null) throw new NotAuthorizationException("RefreshToken 토큰이 존재하지 않습니다.");
		Map<String, String> map = jwtService.validateRefreshToken(cookie);
		if(map.get("code").equals("-1")) {
			throw new InvalidateTokenException("토큰이 만료되었습니다. 다시 로그인해주세요.");
		}
		
		return MessageDTO.builder()
				.code("2")
				.message(map.get("accessToken"))
				.build();
	}
}
