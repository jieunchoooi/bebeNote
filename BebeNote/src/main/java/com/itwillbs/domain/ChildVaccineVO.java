package com.itwillbs.domain;

import java.time.LocalDate;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class ChildVaccineVO { // 자녀 백신접종 기록
	
	private Long child_vaccine_id; // PK, 자녀 백신 접종 ID
	private Long child_id; // FK, 자녀 ID
	private Long vaccine_id; // FK, 백신 ID
	private Long dose_id; // FK, 접종 차수 ID
	private LocalDate vaccinated_date; // 접종한 날짜
	private String hospital; // 접종한 병원
	private String vaccine_product; // 백신 제품명

}
