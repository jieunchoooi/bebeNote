package com.itwillbs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
@RequestMapping("/headerMenu/*")
public class headerMenuController {

	@GetMapping("/bookmark")
	public String bookmark() {
		
		return "/headerMenu/bookmark";
	}
	
	@GetMapping("/nearHospital")
	public String nearHospital() {
		
		return "/headerMenu/nearHospital";
	}
	
	@GetMapping("/openHospital")
	public String openHospital() {
		
		return "/headerMenu/openHospital";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
