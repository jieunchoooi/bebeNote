package com.itwillbs.entity;

import java.security.Timestamp;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.domain.UsersVO;

//@Column(name = "컬럼명",length = 크기, nullable = false, unique, 
//        columnDefinition=varchar(5) 직접지정, insertable, updatable)
//@GeneratedValue(strategy=GenerationType.AUTO) 키값생성, 자동으로 증가
//@Lob,BLOB, CLOB 타입매핑
//@CreateTimestamp insert 시간 자동 저장
//@Enumerated enum 타입매핑 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "User")
@Getter
@Setter
@ToString
public class UserEntity {

	@Id
	@Column(name = "user_id",length = 50)
	private String userId;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Column(name = "provider", nullable = false)
	private String provider;
	
	@Column(name = "created_at", nullable = false)
	private Timestamp created_at;
	
	@Column(name = "updated_at", nullable = false)
	private Timestamp updated_at;
	
//	권한컬럼 추가=> 일반사용자 USER, 관리자 ADMIN
	@Column(name = "role")
	private String role;

	public static UserEntity setMemberEntity(UsersVO usersVO) {
		
		UserEntity user = new UserEntity();
		user.setUserId(usersVO.getUser_id());
		user.setPassword(usersVO.getPassword());
		user.setName(usersVO.getName());
		user.setEmail(usersVO.getEmail());
		user.setPhone(usersVO.getPhone());
		user.setProvider(usersVO.getProvider());
		user.setCreated_at(usersVO.getCreated_at());
		user.setUpdated_at(usersVO.getUpdated_at());

		return user;
	}
	
}