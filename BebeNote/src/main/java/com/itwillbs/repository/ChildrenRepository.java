package com.itwillbs.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itwillbs.entity.Children;


public interface ChildrenRepository extends JpaRepository<Children, Long>{

	Children findByUserId(String userId);

	List<Children> findAllByUserId(String userId);




}

