package com.itwillbs.entity;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.domain.MemberVO;

//@Column(name = "컬럼명",length = 크기, nullable = false, unique, 
//        columnDefinition=varchar(5) 직접지정, insertable, updatable)
//@GeneratedValue(strategy=GenerationType.AUTO) 키값생성, 자동으로 증가
//@Lob,BLOB, CLOB 타입매핑
//@CreateTimestamp insert 시간 자동 저장
//@Enumerated enum 타입매핑 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member implements UserDetails {

	@Id
	@Column(name = "user_id",length = 50)
	private String userId;
	
	@Column(name = "password", nullable = true)
	private String password;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "phone", nullable = true)
	private String phone;
	
	@Column(name = "address", nullable = true)
	private String address;
	
	@Column(name = "detailAddress", nullable = true)
	private String detailAddress;
	
	@Column(name = "provider", nullable = false)
	private String provider;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Timestamp created_at;
	
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Timestamp updated_at;
	
//	권한컬럼 추가=> 일반사용자 USER, 관리자 ADMIN
	@Column(name = "role")
	private String role;
	
	@PrePersist
    public void prePersist() {
        if (this.provider == null) {
            this.provider = "LOCAL";
        }
	}

	
	
	public Member(String user_id, String password, String name, String role, String email,
				  String phone, String address, String detailAddress, String provider) {
		this.userId = user_id;
		this.password = password;
		this.name = name;
		this.role = role;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.detailAddress = detailAddress;
		this.provider = provider;
	}

	public Member() {		}


	public static Member createUser(MemberVO memberVO, PasswordEncoder passwordEncoder) {
		String roleUser = null;
		if(memberVO.getUser_id().equals("admin")) {
			roleUser = "ADMIN";
		}else {
			roleUser = "USER";
		}
		
		return new Member(
			    memberVO.getUser_id(),
			    passwordEncoder.encode(memberVO.getPassword()),
			    memberVO.getName(),
			    roleUser,
			    memberVO.getEmail(),
			    memberVO.getPhone(),
			    memberVO.getAddress(),
			    memberVO.getDetailAddress(),
			    memberVO.getProvider()
			);

	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
	}
	
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return userId;
    }

    public boolean isAccountNonExpired() { // 계정 만료 확인
        return true;
    }

    public boolean isAccountNonLocked() { // 계정 잠긴지 확인
        return true;
    }

    public boolean isCredentialsNonExpired() { // 비밀번호 만료 확인
        return true;
    }

    public boolean isEnabled() { // 계정 활성화 확인
        return true;
    }
	
	
}