package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.domain.MemberVO;
import com.itwillbs.entity.Children;
import com.itwillbs.entity.Member;

public interface ChildrenRepository extends JpaRepository<Children, String>{



}

