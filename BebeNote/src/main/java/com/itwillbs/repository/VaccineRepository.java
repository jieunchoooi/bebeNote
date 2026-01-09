package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.entity.Vaccine;

public interface VaccineRepository extends JpaRepository<Vaccine, Long>{

	

}
