package com.itwillbs.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		
		if(auth != null) {
			// 로그인한 사용자 ID 가져옴
			String userId = auth.getName();
			// DB에서 회원 조회
			Member member = memberService.findByUserId(userId);
			
			if(member != null && member.getAddress() != null) {
				// 회원 정보 model에 담음
				model.addAttribute("userAddress", member.getAddress());
			}
		}
		
		return "/headerMenu/bookmark";
	}
	
	@GetMapping("/nearHospital")
	public String nearHospital(Model model, Authentication auth) {
		System.out.println("headerMenuController nearHospital()");
		
		if(auth != null) {
			// 로그인한 사용자 ID 가져옴
			String userId = auth.getName();
			// DB에서 회원 조회
			Member member = memberService.findByUserId(userId);
			
			if(member != null && member.getAddress() != null) {
				// 회원 정보 model에 담음
				model.addAttribute("userAddress", member.getAddress());
			}
		}
		
		return "/headerMenu/nearHospital";
	}
	
	@GetMapping("/openHospital")
	public String openHospital() {
		System.out.println("headerMenuController openHospital()");
		
		return "/headerMenu/openHospital";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
