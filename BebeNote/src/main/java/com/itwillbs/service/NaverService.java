package com.itwillbs.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.dto.OAuthLoginRequestDTO;
import com.itwillbs.entity.Member;
import com.itwillbs.mapper.MemberMapper;
import com.itwillbs.repository.ChildrenRepository;
import com.itwillbs.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class NaverService {

	private final MemberRepository memberRepository;
	
//	비밀번호 암호화
//	import org.springframework.security.crypto.password.PasswordEncoder;
	private final PasswordEncoder passwordEncoder;
	
	public Member findByUserIdAndProvider(String socialId, String provider) {
		
		return memberRepository.findByUserIdAndProvider(socialId, provider);
	}
	
	public Member joinOAuthUser(OAuthLoginRequestDTO dto) {

		Member member = new Member();
		member.setUserId(dto.getSocialId()); // 네이버고유ID
		member.setProvider(dto.getProvider().toUpperCase()); // naver(대문자로 통일)
		member.setName(dto.getName()); // 이름
		member.setEmail(dto.getEmail()); // 이메일
		member.setRole("USER"); // 일반 사용자 권한 
		
	    member.setAddress("");         // NOT NULL 컬럼 기본값
	    member.setDetailAddress("");   // NOT NULL 컬럼 기본값
	    member.setPhone("");           // NOT NULL 컬럼 기본값
	    member.setPassword(passwordEncoder.encode("oauth_dummy_password")); // 더미 비밀번호
		
		memberRepository.save(member); // DB에 저장
		
		return member;
	}

	
	
}
