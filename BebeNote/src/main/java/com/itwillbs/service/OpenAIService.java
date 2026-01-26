package com.itwillbs.service;

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
			
			// API 요청 Body 생성
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("model", "gpt-4o-mini");
			requestBody.put("messages", List.of(
					Map.of("role", "system", "content", "당신은 소아과 백신 전문가입니다. 아이의 예방접종 일정을 분석하고 다음 접종을 추천해주세요."),
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
		prompt.append(String.format("%s(생후 %d개월)의 예방접종 기록입니다.\n\n", childName, childAge));
		prompt.append("완료한 백신:\n");
		
		if(completedVaccines.isEmpty()) {
			prompt.append("- 아직 접종한 백신이 없습니다.\\n");
		} else {
			for (String vaccine : completedVaccines) {
				prompt.append("- ").append(vaccine).append("\n");
		}
	}
	
	prompt.append("\n다음 접종 일정을 추천해주세요. ");
    prompt.append("접종해야 할 백신명, 권장 시기, 그리고 간단한 설명을 포함해주세요. ");
    prompt.append("친근하고 이해하기 쉬운 말투로 200자 이내로 작성해주세요.");
    
    return prompt.toString();
	}
}
