package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.entity.ChildVaccine;

public interface ChildVaccineRepository extends JpaRepository<ChildVaccine, Long>{

	@Query("SELECT cv FROM ChildVaccine cv WHERE cv.child_id = :child_id")
	List<ChildVaccine> findByChild_id(@Param("child_id") Long child_id); 


}
