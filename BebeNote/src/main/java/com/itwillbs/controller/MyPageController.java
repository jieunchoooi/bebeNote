package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/myPage/*")
public class MyPageController {
	
	@GetMapping("/info")
	public String info() {
		System.out.println("MyPageController info()");
			return "myPage/info";
	}
	

}
