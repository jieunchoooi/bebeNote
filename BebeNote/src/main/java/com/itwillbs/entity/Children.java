package com.itwillbs.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "Children")
@Getter
@Setter
@ToString
public class Children {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "child_id")
	private Long child_id;
	
	@Column(name = "user_id",length = 50)
	private String userId;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "birth_date", nullable = false)
	private LocalDate birth_date;
	
	@Column(name = "gender", nullable = false)
	private String gender;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Timestamp created_at;
	
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Timestamp updated_at;
	
	
	
	
	
	
	
}
