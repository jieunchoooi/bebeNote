package com.itwillbs.domain;

import java.security.Timestamp;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class UsersVO {

	private String user_id;
	private String email;
	private String password;
	private String name;
	private String phone;
	private String provider;
	private String role;
	private Timestamp created_at;
	private Timestamp updated_at;
		
		

}
