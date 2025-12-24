package com.itwillbs.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.entity.UserEntity;
import com.itwillbs.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//		import com.itwillbs.entity.Member;
//		Optional<Member> => orElseThrow(); => null 예외처리
		UserEntity userEntity = memberRepository.findById(id).orElseThrow(() 
				-> new UsernameNotFoundException("없는 회원"));
		
//		Hibernate: 
//		    select
//		        m1_0.id,
//		        m1_0.name,
//		        m1_0.passwd,
//		        m1_0.role 
//		    from
//		        members m1_0 
//		    where
//		        m1_0.id=?
		
		
//		시큐리티    username,password, roles
//		사용자정의   id,      passwd,   role
		UserDetails userDetails = User.builder()
				.username(userEntity.getUserId())
				.password(userEntity.getPassword())
				.roles(userEntity.getRole())
				.build();
		
		return userDetails;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
