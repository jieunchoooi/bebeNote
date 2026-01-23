package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.ChildrenVO;

@Mapper
@Repository
public interface ChildrenMapper {

	List<ChildrenVO> ChildInformation(String userId);

}
