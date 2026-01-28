package com.itwillbs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.entity.Children;


public interface ChildrenRepository extends JpaRepository<Children, Long>{

	Children findByUserId(String userId);

	List<Children> findAllByUserId(String userId);

	@Transactional
	@Modifying
	@Query("UPDATE Children c SET c.child_img = :childImgPath WHERE c.child_id = :childId")
	void updateChildImg(@Param("childId") Long childId, @Param("childImgPath") String childImgPath);


}

