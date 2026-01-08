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
import com.itwillbs.service.MemberService;

import groovy.util.logging.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
public class OAuthController {

    private final MemberService memberService;

    // 콜백 페이지 반환
    @GetMapping("/oauth/naver/callback")
    public String naverCallback() {
        return "oauth/naverCallback";
    }

    // 로그인 처리
    @PostMapping("/member/oauth/naver")
    public ResponseEntity<?> oauthLogin(@RequestBody OAuthLoginRequestDTO dto, HttpServletRequest request) {
        Member member = memberService.findByUserIdAndProvider(dto.getSocialId(), dto.getProvider());

        if (member == null) {
            member = memberService.joinOAuthUser(dto);
        }

        // 로그인 처리
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession().setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        );

        return ResponseEntity.ok().build();
    }
}
