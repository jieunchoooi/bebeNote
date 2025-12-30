package com.itwillbs.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.MemberVO;
import com.itwillbs.entity.Member;
import com.itwillbs.mapper.MemberMapper;
import com.itwillbs.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MemberService {
	
	private final MemberRepository memberRepository;
	
	private final MemberMapper memberMapper;
	
//	비밀번호 암호화
//	import org.springframework.security.crypto.password.PasswordEncoder;
	private final PasswordEncoder passwordEncoder;
	
	
	public void insertSave(MemberVO memberVO) {
		System.out.println("MemberService insertSave()");	

//		비밀번호 암호화, role 권한 부여
		Member member = Member.createUser(memberVO,passwordEncoder);
		
//		com.itwillbs.repository.MemberRepository 인터페이스
		memberRepository.save(member);
	}
	
	

}
