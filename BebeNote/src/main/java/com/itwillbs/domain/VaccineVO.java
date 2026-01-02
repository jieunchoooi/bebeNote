package com.itwillbs.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class VaccineVO {

	private Long vaccine_id; //PK
	private String vaccine_name; //백신이름
}
