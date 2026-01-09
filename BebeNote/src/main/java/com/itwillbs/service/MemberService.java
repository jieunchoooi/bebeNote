package com.itwillbs.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.domain.MemberVO;
import com.itwillbs.dto.OAuthLoginRequestDTO;
import com.itwillbs.entity.Children;
import com.itwillbs.entity.Member;
import com.itwillbs.mapper.MemberMapper;
import com.itwillbs.repository.ChildrenRepository;
import com.itwillbs.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final ChildrenRepository childrenRepository;
	
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


	public void childrenSave(ChildrenVO childrenVO) {
		System.out.println("MemberService childrenSave()");	
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userId = auth.getName(); // 로그인한 user_id
		    
		Children children = new Children();
		
		children.setUserId(userId);
		children.setName(childrenVO.getName());
		children.setBirth_date(childrenVO.getBirth_date());
		children.setGender(childrenVO.getGender());
		
		
		childrenRepository.save(children);
	}


	public boolean isUserIdDuplicate(String userId) {
		
		return memberRepository.existsByUserId(userId);
	}


	public Member findByUserIdAndProvider(String socialId, String provider) {
		
		return memberRepository.findByUserIdAndProvider(socialId, provider);
	}


	public Member joinOAuthUser(OAuthLoginRequestDTO dto) {

		Member member = new Member();
		member.setUserId(dto.getSocialId()); // 네이버고유ID
		member.setProvider(dto.getProvider()); // naver
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
