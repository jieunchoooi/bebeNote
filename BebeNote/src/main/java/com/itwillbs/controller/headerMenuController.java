package com.itwillbs.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itwillbs.dto.BookmarkRequest;
import com.itwillbs.entity.Member;
import com.itwillbs.service.MemberService;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/headerMenu/*")
public class headerMenuController {

	private final MemberService memberService;
	
	@GetMapping("/bookmark")
	public String bookmark(Model model, Authentication auth) {
	    System.out.println("headerMenuController bookmark()");

	    // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
	    if (auth == null || !auth.isAuthenticated()) {
	        model.addAttribute("userBookmark", new ArrayList<>());
	        model.addAttribute("userAddress", null);
	        return "/headerMenu/bookmark";
	    }

	    // 로그인한 사용자 ID 가져옴
	    String userId = auth.getName();

	    // DB에서 회원 조회
	    Member member = memberService.findByUserId(userId);

	    if (member != null && member.getAddress() != null) {
	        // 회원 정보 model에 담음
	        model.addAttribute("userAddress", member.getAddress());
	    }

	    List<BookmarkRequest> userBookmark = memberService.userBookmark(userId);
	    model.addAttribute("userBookmark", userBookmark);

	    return "/headerMenu/bookmark";
	}
	
	@GetMapping("/nearHospital")
	public String nearHospital(Model model, Authentication auth) {
		System.out.println("headerMenuController nearHospital()");
		
		// 로그인하지 않은 경우 로그인 페이지로 리다이렉트
	    if (auth == null || !auth.isAuthenticated()) {
	        model.addAttribute("userBookmark", new ArrayList<>());
	        model.addAttribute("userAddress", null);
	        return "/headerMenu/bookmark";
	    }

	    // 로그인한 사용자 ID 가져옴
	    String userId = auth.getName();

	    // DB에서 회원 조회
	    Member member = memberService.findByUserId(userId);

	    if (member != null && member.getAddress() != null) {
	        // 회원 정보 model에 담음
	        model.addAttribute("userAddress", member.getAddress());
	    }

	    List<BookmarkRequest> userBookmark = memberService.userBookmark(userId);
	    model.addAttribute("userBookmark", userBookmark);
		
		return "/headerMenu/nearHospital";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
