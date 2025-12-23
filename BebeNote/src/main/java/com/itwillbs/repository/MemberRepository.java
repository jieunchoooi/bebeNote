package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.domain.UsersVO;

public interface MemberRepository extends JpaRepository<UsersVO, String>{

	UsersVO findByIdAndPasswd(String id, String password);
	
	
	
	
	
	
	
	
	
}
