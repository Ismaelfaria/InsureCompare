package com.project.InsureCompare.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.InsuranceDTO;
import com.project.InsureCompare.application.mappers.InsuranceMapper;
import com.project.InsureCompare.application.service.interfaces.InsuranceService;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.infra.repository.InsuranceRepository;
import com.project.InsureCompare.util.EntityUpdater;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	private InsuranceRepository insuranceRepository;

	@Autowired
	private InsuranceMapper insuranceMapper;
	
	@Autowired
	private EntityUpdater entityUpdater;

	public List<InsuranceDTO> getAllInsurancesOrderedByPrice() {
		List<Insurance> allInsurance = insuranceRepository.findAllSortedByPrecoBaseAsc();

		return allInsurance.stream().map(insuranceMapper::toDTO).toList();
	}

	public Optional<InsuranceDTO> getInsuranceById(Long id) {
		Optional<Insurance> insuranceOptional= insuranceRepository.findById(id);
				
		if (insuranceOptional.isEmpty()) {
			throw new EntityNotFoundException("Client not found with id: " + id);
		}
		
		 return insuranceOptional.map(insuranceMapper::toDTO);
	}

	public Insurance saveInsurance(InsuranceDTO insuranceDTO) {
		Insurance insurance = insuranceMapper.toEntity(insuranceDTO);
		return insuranceRepository.save(insurance);
	}

	public InsuranceDTO updateInsurance(Long id, Map<String, Object> updateRequest) {
		Optional<Insurance> existingInsuranceOptional = insuranceRepository.findById(id);
		
		Insurance existingInsurance = existingInsuranceOptional.get();
		entityUpdater.updateEntityFields(existingInsurance, updateRequest);
		Insurance insuranceUpdate = insuranceRepository.save(existingInsurance);
		
		return insuranceMapper.toDTO(insuranceUpdate);
	}

	public void deleteInsuranceById(Long id) {
		if (!insuranceRepository.existsById(id)) {
			throw new EntityNotFoundException("Insurance not found with id: " + id);
		}
		insuranceRepository.deleteById(id);
	}
}
