package com.itwillbs.domain;

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
	private String created_at;
	private String updated_at;
		
		

}
