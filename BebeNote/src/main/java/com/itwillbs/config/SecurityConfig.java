package com.itwillbs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final MyUserDetailsService myUserDetailsService;
	
//	암호화 설정
	@Bean
	public PasswordEncoder passwordEncoder() {
//		BCryptPasswordEncoder : 스프링 시큐리터에서 제공하는 클래스 중 하나
//							  : 비밀번호를 암호화하는데 사용할 수 있는 메서드를 가진 클래스
//		BCrypt 해싱 함수를 사용해서 비밀번호를 인코딩해주는 메서드 제공
//		저장소에 저장된 비밀번호의 일치 여부를 확인해주는 메서드 제공
		
		return new BCryptPasswordEncoder();
	}
	
//	권한에 따라서 주소매핑
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		return http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/main/**").permitAll()
						.requestMatchers("/","/insert/**","/insertPro/**","/myPage/**","/headerMenu/**").permitAll()
						.requestMatchers("/index2/**","/member/**","/oauth/**").permitAll()
						.requestMatchers("/oauth/naver/callback").permitAll() 
						.requestMatchers("/img/**","/css/**","/js/**","/uploadPath/**").permitAll()
						.requestMatchers("//t1.daumcdn.net/**").permitAll()
						.requestMatchers("/api/bookmarks/**").permitAll()
						.anyRequest()
						.authenticated()
				)
				.formLogin(form -> form
						.loginPage("/")
						.loginProcessingUrl("/member/login")
						.usernameParameter("user_id")
						.passwordParameter("password")
						.defaultSuccessUrl("/main/main", true)
						.successHandler((request, response, authentication) -> {
					        response.setStatus(HttpServletResponse.SC_OK); // 200
					    })
					    .failureHandler((request, response, exception) -> {
					        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
					        response.setContentType("text/plain; charset=UTF-8");
					        response.getWriter().write("아이디 또는 비밀번호가 올바르지 않습니다.");
					    })
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		                .logoutSuccessUrl("/")
				)	
				.csrf(csrf -> csrf
                .ignoringRequestMatchers("/member/oauth/naver", "/member/checkId", "/member/signup")
				)
				.userDetailsService(myUserDetailsService)
				.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
