package com.team.todaycheck.main.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.team.todaycheck.main.entity.RefreshToken;
import com.team.todaycheck.main.entity.Token;
import com.team.todaycheck.main.exception.FalsifyTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private String secretKey = "myprojectsecret";
	private String refreshKey = "myRefreshKey";
	
	@Autowired UserDetailsService loginService;
	
	// Access ��ū ��ȿ�ð� 30��;
    private long tokenValidTime = 30 * 60 * 1000L;
    // Refresh ��ū ��ȿ�ð� 14�� 14 * 24 * 60 * 60 *
    private long refreshTokenValidTime =  14 * 24 * 60 * 60 * 1000L;
    
    // ��ü �ʱ�ȭ, secretKey�� Base64�� ���ڵ��Ѵ�.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        refreshKey = Base64.getEncoder().encodeToString(refreshKey.getBytes());
    }

    // JWT ��ū ���� 
    public Token createAccessToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload �� ����Ǵ� ��������, ���� ���⼭ user�� �ĺ��ϴ� ���� �ִ´�.
        claims.put("roles", roles); // ������ key / value ������ ����ȴ�.
        Date now = new Date();
        
        String accessToken = Jwts.builder()
                .setClaims(claims) // ���� ����
                .setIssuedAt(now) // ��ū ���� �ð� ����
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // ����� ��ȣȭ �˰����
                // signature �� �� secret�� ����
                .compact();
        
        // RefreshToken �߱�
        String refreshToken =  Jwts.builder()
                .setClaims(claims) // ���� ����
                .setIssuedAt(now) // ��ū ���� �ð� ����
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, refreshKey)  // ����� ��ȣȭ �˰����
                // signature �� �� secret�� ����
                .compact();

        return Token.builder().accessToken(accessToken).refreshToken(refreshToken).key(userPk).build();
    }
    
    // refreshToken ��ȿ�� �˻�
    public String validateRefreshToken(RefreshToken refreshTokenObj){
        // refresh ��ü���� refreshToken ����
        String refreshToken = refreshTokenObj.getRefreshToken();
        try {
            // ����
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshKey).parseClaimsJws(refreshToken);

            //refresh ��ū�� ����ð��� ������ �ʾ��� ���, ���ο� access ��ū�� �����մϴ�.
            if (!claims.getBody().getExpiration().before(new Date())) {
                return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
            }
        }catch (Exception e) {
            return null; //refresh ��ū�� ����Ǿ��� ���, �α����� �ʿ��մϴ�.
        }
        return null;
    }
    
    // Access��ū �� ����
    public String recreationAccessToken(String userEmail, Object roles){
        Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload �� ����Ǵ� ��������
        claims.put("roles", roles); // ������ key / value ������ ����ȴ�.
        Date now = new Date();
        //Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims) // ���� ����
                .setIssuedAt(now) // ��ū ���� �ð� ����
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // ����� ��ȣȭ �˰����
                // signature �� �� secret�� ����
                .compact();

        return accessToken;
    }
    
    // JWT ��ū���� ���� ���� ��ȸ
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loginService.loadUserByUsername(this.getUserPk(token));
        if(userDetails == null) {
        	throw new FalsifyTokenException("�� �� ���� ��ū�̰ų� , �����Ǿ����ϴ�.");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // ��ū���� ȸ�� ���� ����
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request�� Header���� token ���� �����ɴϴ�. "Authorization" : "TOKEN��'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // ��ū�� ��ȿ�� + �������� Ȯ��
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
