package com.itwillbs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.domain.ChildVaccineVO;
import com.itwillbs.entity.ChildVaccine;
import com.itwillbs.repository.ChildVaccineRepository;
import com.itwillbs.repository.VaccineDoseRepository;
import com.itwillbs.repository.VaccineRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class MyPageService {
	
	private final ChildVaccineRepository childVaccineRepository;
	private final VaccineRepository vaccineRepository;
	private final VaccineDoseRepository vaccineDoseRepository;
	

	public void saveVaccine(ChildVaccineVO vo) {
		System.out.println("MyPageService saveVaccine()");
		
		ChildVaccine entity = new ChildVaccine();
        entity.setChild_id(vo.getChild_id());
        entity.setVaccine_id(vo.getVaccine_id());
        entity.setDose_id(vo.getDose_id());
        entity.setVaccinated_date(vo.getVaccinated_date());
        entity.setHospital(vo.getHospital());
        entity.setVaccine_product(vo.getVaccine_product());
		
		childVaccineRepository.save(entity);
	}

	public Object findAllVaccines() {
		return vaccineRepository.findAll();
	}

	public Object findAllDoses() {
		return vaccineDoseRepository.findAll();
	}

	public List<ChildVaccine> findChildRecords(Long child_id) {
		// TODO Auto-generated method stub
		return childVaccineRepository.findByChild_id(child_id);
	}

	

}
