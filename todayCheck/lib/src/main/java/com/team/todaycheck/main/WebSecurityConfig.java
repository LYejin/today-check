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
        // API Client Id �ҷ�����
        String clientId = env.getProperty(
                CLIENT_PROPERTY_KEY + client + ".client-id");

        // API Client Id ���� �����ϴ��� Ȯ���ϱ�
        if (clientId == null) {
            return null;
        }

        // API Client Secret �ҷ�����
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
		http.csrf().disable()	// Post ��û block ����
        .authorizeRequests() // �ش� �޼ҵ� �Ʒ��� �� ��ο� ���� ������ ������ �� �ִ�.
        	.antMatchers("/admin/**").authenticated() // ������ �ǽ�
            .antMatchers("/admin/**").hasRole("ADMIN") // ��ȣ�� ������ ���� ������ ���ٰ���, ROLE_�� �پ ���� ��. ��, ���̺� ROLE_���Ѹ� ���� �����ؾ� ��.
            .antMatchers("/user/**").authenticated() // ������ �ǽ�
            .antMatchers("/user/**").hasRole("USER")
            .antMatchers("/post/post").hasRole("USER")
            .antMatchers("/post/post/**").hasRole("USER")
            .antMatchers("/post/comment/**").authenticated()
            .antMatchers("/post/comment/**").hasRole("USER")
            .antMatchers("/**").permitAll() // �̿� ��û�� ������ ����
            .anyRequest().authenticated()  //  �α��ε� ����ڰ� ��û�� ������ �� �ʿ��ϴ�  ���� ����ڰ� �������� �ʾҴٸ�, ������ ��ť��Ƽ ���ʹ� ��û�� ��Ƴ��� ����ڸ� �α��� �������� �����̷��� ���ش�.
            .and()
         .logout()
             .permitAll()
             // .logoutUrl("/logout") // �α׾ƿ� url
             .deleteCookies("refreshToken")
             // .logoutSuccessUrl("/")
             .and()
             .oauth2Login()
				.loginPage("/refresh") // �ΰ����� ���� ���� ��
				.clientRegistrationRepository(clientRegistrationRepository())
				.authorizedClientService(authorizedClientService())
				.and()
         .exceptionHandling()
			.accessDeniedPage("/accessDenied_page"); // ������ ���� ����� �������õ����� ��
		
		http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), // ����
				UsernamePasswordAuthenticationFilter.class);
	}
}
