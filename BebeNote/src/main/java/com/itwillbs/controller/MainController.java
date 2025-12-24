package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/main/*")
public class MainController {


	
	@GetMapping("/main")
	public String main() {
		return "main";
	}

	
	
	
	
}
