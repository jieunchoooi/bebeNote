package com.itwillbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.itwillbs.dto.BookmarkRequest;

@Mapper
@Repository
public interface MemberMapper {

	List<BookmarkRequest> userBookmark(String userId);

}
