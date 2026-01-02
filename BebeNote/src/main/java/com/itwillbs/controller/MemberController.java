package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.domain.MemberVO;
import com.itwillbs.service.MemberService;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/member/*")
public class MemberController {

	private final MemberService memberService;
	
	
	@PostMapping("/insertPro")
	public String insertPro(MemberVO memberVO) {
		System.out.println("MemberController insertPro()");
		System.out.println(memberVO);

		memberService.insertSave(memberVO);
		
		return "redirect:/main/main";
	}
	
//	@PostMapping("/login")
//	public String login() {
//		System.out.println("MemberController login()");
//		
//		return "redirect:/main/main";
//	}
	
	@PostMapping("/addChild")
	public String addChild(ChildrenVO childrenVO) {
		System.out.println("MemberController addChild()");
		System.out.println(childrenVO);
		
		memberService.childrenSave(childrenVO);
		
		return "redirect:/main/main";
	}
	
	@ResponseBody
	@GetMapping("/checkId")
	public boolean checkId(@RequestParam("userId") String userId) {
		System.out.println("MemberController checkId()");
		return memberService.isUserIdDuplicate(userId);
	}
	
	
	
	
	
	
}
