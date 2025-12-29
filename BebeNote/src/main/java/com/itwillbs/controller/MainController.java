package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
public class MainController {


	@GetMapping("/")
	public String index() {
		return "/main/main";
	}
	
	@GetMapping("/main/main")
	public String main() {
		return "/main/main";
	}
	
	@GetMapping("/main/reservation")
	public String reservation() {
		return "/main/reservation";
	}

	
	
	
	
}
