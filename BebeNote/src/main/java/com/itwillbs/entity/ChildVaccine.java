package com.itwillbs.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ChildVaccine")
@Getter
@Setter
@ToString
public class ChildVaccine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "child_vaccine_id", nullable = false, updatable = false)
	private Long child_vaccine_id; // PK, 자녀 백신 접종 ID
	
	@Column
	private Long child_id; // FK, 자녀 ID
	
	@Column
	private Long vaccine_id; // FK, 백신 ID
	
	@Column
	private Long dose_id; // FK, 접종 차수 ID
	
	@Column
	private LocalDate vaccinated_date; // 접종한 날짜
	
	@Column
	private String hospital; // 접종한 병원
	
	@Column
	private String vaccine_product; // 백신 제품명

}
