package com.team.todaycheck.main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.team.todaycheck.main.oauth.CustomOAuth2UserService;
import com.team.todaycheck.main.oauth.OAuth2Provider;
import com.team.todaycheck.main.security.JwtAuthenticationFilter;
import com.team.todaycheck.main.security.JwtTokenProvider;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";
    private static List<String> clients = Arrays.asList("google", "naver");
    @Resource private Environment env;
    @Autowired CustomOAuth2UserService customOAuth2UserService;
    
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = clients.stream()
                .map(c -> getRegistration(c))
                .filter(registration -> registration != null)
                .collect(Collectors.toList());
        return new InMemoryClientRegistrationRepository(registrations);
    }
    
    private ClientRegistration getRegistration(String client) {
        // API Client Id 불러오기
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");

        // API Client Id 값이 존재하는지 확인하기
        if (clientId == null) {
            return null;
        }

        // API Client Secret 불러오기
        String clientSecret = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-secret");

        if (client.equals("google")) {
            return OAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }
        
        if (client.equals("naver")) {
            return OAuth2Provider.NAVER.getBuilder(client)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }

        return null;
    }
    

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
	}
	
	// https://taesan94.tistory.com/109
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()	// Post 요청 block 제거
        .authorizeRequests() // 해당 메소드 아래는 각 경로에 따른 권한을 지정할 수 있다.
        	.antMatchers("/admin/**").authenticated() // 인증을 실시
            .antMatchers("/admin/**").hasRole("ADMIN") // 괄호의 권한을 가진 유저만 접근가능, ROLE_가 붙어서 적용 됨. 즉, 테이블에 ROLE_권한명 으로 저장해야 함.
            .antMatchers("/user/**").authenticated() // 인증을 실시
            .antMatchers("/user/**").hasRole("USER")
            .antMatchers("/post/post").hasRole("USER")
            .antMatchers("/post/post/**").hasRole("USER")
            .antMatchers("/post/comment/**").authenticated()
            .antMatchers("/post/comment/**").hasRole("USER")
            .antMatchers("/**").permitAll() // 이외 요청은 누구나 가능
            .anyRequest().authenticated()  //  로그인된 사용자가 요청을 수행할 떄 필요하다  만약 사용자가 인증되지 않았다면, 스프링 시큐리티 필터는 요청을 잡아내고 사용자를 로그인 페이지로 리다이렉션 해준다.
            .and()
         .logout()
             .permitAll()
             // .logoutUrl("/logout") // 로그아웃 url
             .deleteCookies("refreshToken")
             // .logoutSuccessUrl("/")
             .and()
             .oauth2Login()
				.loginPage("/refresh") // 인가되지 않은 접근 시
				.clientRegistrationRepository(clientRegistrationRepository())
				.authorizedClientService(authorizedClientService())
				.and()
         .exceptionHandling()
			.accessDeniedPage("/accessDenied_page"); // 권한이 없는 대상이 접속을시도했을 때
		
		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), // 필터
				UsernamePasswordAuthenticationFilter.class);
	}
}
