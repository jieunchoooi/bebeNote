package com.itwillbs.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class VaccineDoseVO { //백신 접종 차수 

	private Long dose_id; //PK, 접종차수ID
	private Long vaccine_id; // 백신ID
	private Integer dose_order; // 접종순서 1,2,3,4,5,6
	private String dose_label; // 1차/2차/3차/추가1차/추가2차/추가3차
}
