package com.itwillbs.domain;

import java.security.Timestamp;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class ChildrenVO {

	private String child_id;
	private String user_id; // 부모 ID
	private String name;
	private String birth_date;
	private String gender;
	private Timestamp created_at;
	private Timestamp updated_at;
	
	
	
	
	
	
}
