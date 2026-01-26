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

	    // 로그인 사용자 ID
	    String userId = SecurityContextHolder.getContext().getAuthentication().getName();

	    // 자녀 정보
	    List<ChildrenVO> children = mainService.ChildInformation(userId);

	    // 즐겨찾기 정보
	    List<BookmarkRequest> userBookmark = memberService.userBookmark(userId);

	    // 공통 모델
	    model.addAttribute("userBookmark", userBookmark);
	    model.addAttribute("children", children);

	    // 자녀가 없는 경우 (여기서 바로 종료)
	    if (children == null || children.isEmpty()) {
	        model.addAttribute("hasChild", false);

	        // 화면에서 필요할 수 있는 기본 데이터
	        model.addAttribute("vaccines", myPageService.findAllVaccines());
	        model.addAttribute("doses", myPageService.findAllDoses());
	        model.addAttribute("vaccineRecords", new HashMap<>());

	        return "/main/main";
	    }

	    // 자녀가 있는 경우
	    model.addAttribute("hasChild", true);

	    // 첫 번째 자녀 기준
	    Long childId = children.get(0).getChild_id();

	    // 접종 기록 조회
	    List<ChildVaccine> records = myPageService.findChildRecords(childId);

	    // 접종 기록 Map 변환 (백신ID_차수ID)
	    Map<String, ChildVaccine> vaccineRecords = new HashMap<>();
	    for (ChildVaccine record : records) {
	        String key = record.getVaccine_id() + "_" + record.getDose_id();
	        vaccineRecords.put(key, record);
	    }

	    // 모델 추가
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
