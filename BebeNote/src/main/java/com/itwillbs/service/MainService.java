package com.itwillbs.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.boot.json.JsonWriter.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.domain.MemberVO;
import com.itwillbs.mapper.ChildrenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MainService {
	
	private final ChildrenMapper childrenMapper ;

	public List<ChildrenVO> ChildInformation(String user_id) {
		System.out.println("MainService ChildInformation()");
		
		// 자녀 개월수 구하기
		// db에서 자녀 정보 가져오기
		List<ChildrenVO> children = childrenMapper.ChildInformation(user_id);
		// 오늘 날짜 구하기
		LocalDate today = LocalDate.now();
		
		for(ChildrenVO child : children) {
			if(child.getBirth_date() != null) {
				// Period: 두 날짜 사이의 기간을 나타내는 클래스
				Period period = Period.between(child.getBirth_date(), today);
				int months = period.getYears() * 12 + period.getMonths();
				child.setAgeInMonths(months);
			}
		}
		
		
		return children;
	}

	


	

	
	
	
	
	
	
	
	
	
	
	
	
}
