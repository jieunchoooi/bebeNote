package com.itwillbs.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.mapper.MemberMapper;
import com.itwillbs.repository.BookmarkRepository;
import com.itwillbs.repository.ChildrenRepository;
import com.itwillbs.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@Log
public class OpenAIService {

	@Value("${openai.api.key}")
	private String apiKey;
	
	@Value("${openai.api.url}")
	private String apiUrl;
	
	private final WebClient webClient; 
	private final ObjectMapper objectMapper; 
	
	public OpenAIService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
		this.webClient = webClientBuilder.build();
		this.objectMapper = objectMapper;
	}
	
	public String getVaccineRecommendation(String childName, Integer childAge, List<String> completedVaccines) {
		try {
			// 프롬프트 생성
			String prompt = createPrompt(childName, childAge, completedVaccines);
			LocalDate today = LocalDate.now();
			String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		    log.info("오늘 기준 프롬프트:\n{}" +  prompt);
		    
			// API 요청 Body 생성
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("model", "gpt-4o-mini");
			requestBody.put("messages", List.of(
					Map.of("role", "system", "content", "너는 소아과 예방접종 전문가야.\n" +
			                "사용자가 준 접종 기록을 보고 오늘 날짜(" + todayStr + ") 기준 다음 접종을 1개만 추천해줘.\n" +
			                "출력은 2문장만, 형식은 아래와 같이 정확히 맞춰줘:\n" +
			                "다음 접종은 [백신명] [차수]입니다.\n" +
			                "권장 시기는 [기준] 후 [기간], 즉 [날짜]에 맞는 게 좋아요.\n" +
			                "사용자 접종 기록:\n" ),
					Map.of("role", "user", "content", prompt)
			));
			requestBody.put("max_tokens", 500);
	        requestBody.put("temperature", 0.7);
			
	        // API 호출
	        String response = webClient.post()
	        		.uri(apiUrl)
	        		.header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
	        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
	        		.bodyValue(requestBody)
	        		.retrieve()
	        		.bodyToMono(String.class)
	        		.block();
	        
	        // 응답 피싱
	        JsonNode jsonNode = objectMapper.readTree(response);
	        return jsonNode.path("choices").get(0).path("message").path("content").asText();
		
		}catch (Exception e) {
			e.printStackTrace();
			return "AI 추천을 가져오는 중 오류가 발생했습니다.";
		}
	}
	
	
	private String createPrompt(String childName, Integer childAge, List<String> completedVaccines) {
		StringBuilder prompt = new StringBuilder();

	    prompt.append("너는 소아과 예방접종 전문가야.\n");
	    prompt.append("아래 정보로 다음 접종을 한 가지만 추천해줘.\n");
	    prompt.append("출력은 딱 2문장만, 불필요한 설명/인사말은 절대 금지.\n");
	    prompt.append("형식은 아래처럼 정확히 맞춰줘:\n");
	    prompt.append("다음 접종은 [백신명] [차수]입니다.\n");
	    prompt.append("권장 시기는 [기준] 후 [기간], 즉 [날짜]에 맞는 게 좋아요.\n");

	    prompt.append(String.format("아이 정보: %s(생후 %d개월)\n", childName, childAge));
	    prompt.append("완료한 백신:\n");

	    if (completedVaccines.isEmpty()) {
	        prompt.append("- 아직 접종한 백신이 없습니다.\n");
	    } else {
	        for (String vaccine : completedVaccines) {
	            prompt.append("- ").append(vaccine).append("\n");
	        }
	    }

	    return prompt.toString();
	}
}
