package com.itwillbs.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.dto.BookmarkRequest;
import com.itwillbs.service.MemberService;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/api/bookmark")
public class BookmarkApiController {

	  private final MemberService memberService;
	  
	  // 즐겨찾기 토글
	  @PostMapping("/toggle")
	  @ResponseBody
	  public Map<String, Object> toggleBookmark(@RequestBody BookmarkRequest request, Authentication auth){
		  
		  return memberService.toggleBookmark(request, auth);
	  }
	
}
