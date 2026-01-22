package com.itwillbs.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.domain.MemberVO;
import com.itwillbs.dto.BookmarkRequest;
import com.itwillbs.dto.OAuthLoginRequestDTO;
import com.itwillbs.entity.Bookmark;
import com.itwillbs.entity.Children;
import com.itwillbs.entity.Member;
import com.itwillbs.mapper.MemberMapper;
import com.itwillbs.repository.BookmarkRepository;
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
	private final BookmarkRepository bookmarkRepository;
	
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


	public Member processOAuthUser(OAuthLoginRequestDTO userInfo) {
		
		// 기존 회원 조회
		Optional<Member> optionalMember = memberRepository.findByProviderAndUserId(userInfo.getProvider(), userInfo.getSocialId());
		
		// 기존 회원이면 그대로 반환
		if(optionalMember.isPresent()) {
			return optionalMember.get();
		}
		
		// 신규면 가입
		Member member = new Member();
		member.setUserId(userInfo.getSocialId());   
		member.setProvider(userInfo.getProvider());
		member.setEmail(userInfo.getEmail());
		member.setName(userInfo.getName());
		member.setRole("USER");
		member.setAddress("");         // NOT NULL 컬럼 기본값
	    member.setDetailAddress("");   // NOT NULL 컬럼 기본값
	    member.setPhone("");           // NOT NULL 컬럼 기본값
	    member.setPassword(passwordEncoder.encode("oauth_dummy_password")); // 더미 비밀번호

		return memberRepository.save(member);
	}


	public Member findByUserId(String userId) {
		
		return memberRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
	}


	// 즐겨찾기 추가 
	public Map<String, Object> toggleBookmark(BookmarkRequest request, Authentication auth) {
		
		String userId = auth.getName();
		
		// 기존 즐겨찾기 있는지 확인
        Optional<Bookmark> existing = bookmarkRepository.findByUserIdAndKakaoPlaceId(userId, request.getKakaoPlaceId());

        Map<String, Object> result = new HashMap<>();

        if (existing.isPresent()) {
            // 이미 즐겨찾기 되어 있으면 삭제 또는 토글
            bookmarkRepository.delete(existing.get());
            result.put("success", true);
            result.put("action", "removed"); // 취소됨
		}else {
			// 즐겨찾기에 없으면 새로 저장
			Bookmark bookmark = new Bookmark();
			bookmark.setUserId(userId);
			bookmark.setHospitalName(request.getHospitalName());
			bookmark.setAddress(request.getAddress());
			bookmark.setPhone(request.getPhone());
			bookmark.setKakaoPlaceId(request.getKakaoPlaceId());
			bookmark.setLatitude(request.getLatitude());
			bookmark.setLongitude(request.getLongitude());
			bookmark.setBookmarked(request.isBookmarked());
	
			bookmarkRepository.save(bookmark);
			
			result.put("success", true);
            result.put("action", "added"); // 추가됨
		}
		
	    return result;
	}


	

}
