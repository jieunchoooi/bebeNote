package com.itwillbs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.itwillbs.entity.ChildVaccine;
import com.itwillbs.entity.Vaccine;
import com.itwillbs.entity.VaccineDose;
import com.itwillbs.service.MainService;
import com.itwillbs.service.MemberService;
import com.itwillbs.service.MyPageService;

import groovy.util.logging.Log;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
public class MainController {

	private final MainService mainService;
	private final MemberService memberService;
	private final MyPageService myPageService;

	@GetMapping("/")
	public String index() {
		return "redirect:/main/main";
	}
	
	@GetMapping("/main/main")
	public String main(Model model, Authentication auth) {
		System.out.println("MainController main()");
		
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		// 자녀 정보
		List<ChildrenVO> children = mainService.ChildInformation(userId);
		// 즐겨찾기 정보
		List<BookmarkRequest> userBookmark = memberService.userBookmark(userId);
		
		Long childId = children.get(0).getChild_id();
		List<ChildVaccine> records = myPageService.findChildRecords(childId);
		    
		Map<String, ChildVaccine> vaccineRecords = new HashMap<>();
		for (ChildVaccine record : records) {
		    String key = record.getVaccine_id() + "_" + record.getDose_id();
		    vaccineRecords.put(key, record);
		}
		
		model.addAttribute("userBookmark", userBookmark);
		model.addAttribute("children", children);
		// 접종수첩 정보
		model.addAttribute("vaccines", myPageService.findAllVaccines());
	    model.addAttribute("doses", myPageService.findAllDoses());
	    model.addAttribute("vaccineRecords", vaccineRecords);
		    
		return "/main/main";
	}
	
	@GetMapping("/main/reservation")
	public String reservation() {
		return "/main/reservation";
	}

	
	
	
	
}
