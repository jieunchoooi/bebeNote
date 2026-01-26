package com.itwillbs.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.dto.AIRecommendationResponse;
import com.itwillbs.service.AIRecommendationService;
import com.itwillbs.service.MainService;
import com.itwillbs.service.MemberService;
import com.itwillbs.service.MyPageService;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Log
public class AIRecommendationController {

	private final AIRecommendationService aiRecommendationService;
    private final MainService mainService;

    @GetMapping("/api/ai-recommendation/{childId}")
    @ResponseBody
    public AIRecommendationResponse getRecommendation(@PathVariable Long childId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ChildrenVO> children = mainService.ChildInformation(userId);
        
        ChildrenVO child = children.stream()
            .filter(c -> c.getChild_id().equals(childId))
            .findFirst()
            .orElse(children.get(0));
        
        String recommendation = aiRecommendationService.getRecommendation(childId, child);
        
        AIRecommendationResponse response = new AIRecommendationResponse();
        response.setRecommendation(recommendation);
        
        return response;
    }
}
