package com.itwillbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.domain.MemberVO;
import com.itwillbs.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String>{

	MemberVO findByUserIdAndPassword(String user_id, String password);

	boolean existsByUserId(String userId);

	Member findByUserIdAndProvider(String userId, String provider);

	Optional<Member> findByUserId(String username);


	
}

