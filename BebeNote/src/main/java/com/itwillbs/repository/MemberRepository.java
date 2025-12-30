package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.domain.MemberVO;
import com.itwillbs.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{

	MemberVO findByUserIdAndPassword(String user_id, String password);
	
	
	
	
	
	
	
	
	
}
