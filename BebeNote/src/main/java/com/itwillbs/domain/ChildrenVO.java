package com.itwillbs.domain;

import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class ChildrenVO {

	private Long child_id;
	private String user_id; // 부모 ID
	private String name;
	private LocalDate birth_date;
	private String gender;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	private int ageInMonths; // 계산된 개월수
	
	
	
	
	
	
}
