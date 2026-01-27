package com.itwillbs.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.domain.ChildrenVO;
import com.itwillbs.entity.ChildVaccine;
import com.itwillbs.entity.Vaccine;
import com.itwillbs.entity.VaccineDose;
import com.itwillbs.repository.ChildVaccineRepository;
import com.itwillbs.repository.VaccineDoseRepository;
import com.itwillbs.repository.VaccineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class AIRecommendationService {

	private final VaccineRepository vaccineRepository;
	private final VaccineDoseRepository vaccineDoseRepository;
    private final ChildVaccineRepository childVaccineRepository;
	private final OpenAIService openAIService;
    private final MyPageService myPageService;
	
	public String getRecommendation(Long childId, ChildrenVO child) {
		// 아이의 나이 계산(개월수)
		Integer childAge = calculateAgeInMonths(child.getBirth_date());
		// 완료한 백신 목록 가져오기
		List<String> completedVaccines = getCompletedVaccines(childId);
		// OpenAI API 호출
		return openAIService.getVaccineRecommendation(child.getName(), childAge, completedVaccines);
	}
	
    private Integer calculateAgeInMonths(LocalDate birthDate) {
        Period period = Period.between(birthDate, LocalDate.now());
        return period.getYears() * 12 + period.getMonths();
    }
	
    private List<String> getCompletedVaccines(Long childId) {
        List<ChildVaccine> records = childVaccineRepository.findByChild_id(childId);
        List<Vaccine> vaccines = vaccineRepository.findAll();
        List<VaccineDose> allDoses = vaccineDoseRepository.findAll();
        
        Map<Long, String> vaccineNames = new HashMap<>();
        for (Vaccine v : vaccines) {
            vaccineNames.put(v.getVaccine_id(), v.getVaccine_name());
        }
        
        // vaccine_id와 dose_order로 매핑 
        Map<String, String> doseLabels = new HashMap<>();
        for (VaccineDose d : allDoses) {
            String key = d.getVaccine_id() + "_" + d.getDose_order();
            doseLabels.put(key, d.getDose_label());
        }
        
        List<String> completed = new ArrayList<>();
        for (ChildVaccine record : records) {
            String vaccineName = vaccineNames.get(record.getVaccine_id());
            // dose_id를 그대로 dose_order로 사용
            String key = record.getVaccine_id() + "_" + record.getDose_id();
            String doseLabel = doseLabels.get(key);
            
            if (vaccineName != null && doseLabel != null) {
                completed.add(vaccineName + " " + doseLabel);
            }
        }
        
        return completed;
    }
    
	
}
