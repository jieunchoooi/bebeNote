package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.entity.Children;
import com.itwillbs.repository.ChildrenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Service
@RequiredArgsConstructor
@Log
public class ChildrenService {
	
	private final ChildrenRepository childrenRepository;
	
	public Children findByUserId(String userId) {
		
		return childrenRepository.findByUserId(userId);
	}

	public List<Children> findAllByUserId(String userId) {
		
		return childrenRepository.findAllByUserId(userId);
	}

}
