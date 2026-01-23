package com.itwillbs.controller;

import java.util.List;

import org.springframework.boot.json.JsonWriter.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.dto.BookmarkRequest;
import com.itwillbs.service.MainService;
import com.itwillbs.service.MemberService;

import groovy.util.logging.Log;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
public class MainController {

	private final MainService mainService;
	private final MemberService memberService;

	@GetMapping("/")
	public String index() {
		return "redirect:/main/main";
	}
	
	@GetMapping("/main/main")
	public String main(Model model, Authentication auth) {
		System.out.println("MainController main()");
		
		String userId = auth.getName();
		List<ChildrenVO> children = mainService.ChildInformation(userId);
		
		List<BookmarkRequest> userBookmark = memberService.userBookmark(userId);

		model.addAttribute("userBookmark", userBookmark);
		model.addAttribute("children", children);
		
		return "/main/main";
	}
	
	@GetMapping("/main/reservation")
	public String reservation() {
		return "/main/reservation";
	}

	
	
	
	
}
