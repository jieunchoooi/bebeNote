package com.itwillbs.entity;

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
@Table(name = "VaccineDose")
@Getter
@Setter
@ToString
public class VaccineDose {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dose_id", nullable = false, updatable = false)
	private Long dose_id; //PK, 접종차수ID
	
	@Column(name = "vaccine_id", nullable = false)
	private Long vaccine_id; // 백신ID
	
	@Column(name = "dose_order", nullable = false)
	private Integer dose_order; // 접종순서 1,2,3,4,5,6
	
	@Column(name = "dose_label", length = 50)
	private String dose_label; // 1차/2차/3차/추가1차/추가2차/추가3차

}
