package com.itwillbs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.dto.OAuthLoginRequestDTO;
import com.itwillbs.entity.Member;
import com.itwillbs.service.KakaoService;
import com.itwillbs.service.MemberService;
import com.itwillbs.service.NaverService;

import groovy.util.logging.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
public class OAuthController {

    private final MemberService memberService;
    private final NaverService naverService;
    private final KakaoService kakaoService;

    // 네이버 콜백 페이지 반환
    @GetMapping("/oauth/naver/callback")
    public String naverCallback() {
        return "oauth/naverCallback";
    }

    // 네이버 로그인 처리
    @PostMapping("/member/oauth/naver")
    public ResponseEntity<?> oauthLogin(@RequestBody OAuthLoginRequestDTO dto, HttpServletRequest request) {
    	// DB에서 가입된 회원 찾기
        Member member = naverService.findByUserIdAndProvider(dto.getSocialId(), dto.getProvider());

        // DB에 없으면 가입
        if (member == null) {
            member = naverService.joinOAuthUser(dto);
        }

        // 로그인 처리 (Spring Security 세션에 저장)
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());

        // 세션에 Security Context 저장
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        );

        return ResponseEntity.ok().build();
    }
    
    // 카카오 콜백 페이지 반환
    @GetMapping("/oauth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code, HttpServletRequest request) {
    	
    	// 인증코드로 토큰 받기
    	String accessToken = kakaoService.getAccessToken(code);
    	// 토큰으로 사용자 정보 가져오기
    	OAuthLoginRequestDTO userInfo = kakaoService.getUserInfo(accessToken);
    	
        // 회원 조회 / 가입
        Member member = memberService.processOAuthUser(userInfo);

    	// 로그인 처리 (Spring Security 세션에 저장)
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
        
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        );
    	
        return "redirect:/main/main";
    }
    
    
    
    
}
