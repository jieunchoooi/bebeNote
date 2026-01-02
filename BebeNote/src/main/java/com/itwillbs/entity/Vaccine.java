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
@Table(name = "Vaccine")
@Getter
@Setter
@ToString
public class Vaccine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vaccine_id",nullable = false, updatable = false)
	private Long vaccine_id; //PK
	
	@Column(name = "vaccine_name",length = 50)
	private String vaccine_name; //백신이름

}
