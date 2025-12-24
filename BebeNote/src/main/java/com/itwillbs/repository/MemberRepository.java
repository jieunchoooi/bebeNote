package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.domain.UsersVO;
import com.itwillbs.entity.UserEntity;

public interface MemberRepository extends JpaRepository<UserEntity, String>{

	UsersVO findByUserIdAndPassword(String user_id, String password);
	
	
	
	
	
	
	
	
	
}
