package com.itwillbs.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.ChildVaccineVO;
import com.itwillbs.entity.Children;
import com.itwillbs.service.ChildrenService;
import com.itwillbs.service.MyPageService;

import groovy.util.logging.Log;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/myPage/*")
public class MyPageController {
	
	private final MyPageService myPageService;
	private final ChildrenService childrenService;
	
	@GetMapping("/info")
	public String info(HttpSession session, Model model) {
		System.out.println("MyPageController info()");
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		
		List<Children> children = childrenService.findAllByUserId(userId);
		if (children == null) {
            model.addAttribute("noChild", true);
            return "myPage/info";
        }
		
		model.addAttribute("children", children);
		model.addAttribute("vaccines", myPageService.findAllVaccines());
	    model.addAttribute("doses", myPageService.findAllDoses());
		
			return "myPage/info";
	}
	
	@PostMapping("/saveVaccine")
	@ResponseBody
	public void saveVaccine(@RequestBody ChildVaccineVO vo) {
		System.out.println("MyPageController saveVaccine()");
		myPageService.saveVaccine(vo);
	}
	
	@GetMapping("/myHospital")
	public String myHospital() {
		System.out.println("MyPageController myHospital()");
			return "myPage/myHospital";
	}
	
	@GetMapping("/payHistory")
	public String payHistory() {
		System.out.println("MyPageController payHistory()");
			return "myPage/payHistory";
	}
	
	@GetMapping("/payControl")
	public String payControl() {
		System.out.println("MyPageController payControl()");
			return "myPage/payControl";
	}
	

}
