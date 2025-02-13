package com.project.InsureCompare.application.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.InsuranceDTO;
import com.project.InsureCompare.application.mappers.InsuranceMapper;
import com.project.InsureCompare.application.service.interfaces.InsuranceService;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.infra.repository.InsuranceRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InsuranceServiceImpl implements InsuranceService {

	@Autowired
	private InsuranceRepository insuranceRepository;

	@Autowired
	private InsuranceMapper insuranceMapper;

	public List<InsuranceDTO> getAllInsurancesOrderedByPrice() {
		List<Insurance> allInsurance = insuranceRepository.findAllSortedByPrecoBaseAsc();

		return allInsurance.stream().map(insuranceMapper::toDTO).toList();
	}

	public InsuranceDTO getInsuranceById(Long id) {
		return insuranceRepository.findById(id).map(insuranceMapper::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));
	}

	public Insurance saveInsurance(InsuranceDTO insuranceDTO) {
		Insurance insurance = insuranceMapper.toEntity(insuranceDTO);
		return insuranceRepository.save(insurance);
	}

	public InsuranceDTO updateInsurance(Long id, Map<String, Object> updateRequest) {
		Insurance existingInsurance = insuranceRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));

		updateInsuranceFields(existingInsurance, updateRequest);

		Insurance insuranceUpdate = insuranceRepository.save(existingInsurance);
		return insuranceMapper.toDTO(insuranceUpdate);
	}

	public void updateInsuranceFields(Insurance existingInsurance, Map<String, Object> updateRequest) {
		updateRequest.forEach((field, newValue) -> {
			switch (field.toLowerCase()) {
			case "type":
				existingInsurance.setType((String) newValue);
				break;
			case "baseprice":
				existingInsurance.setBasePrice((Double) newValue);
				break;
			default:
				throw new IllegalArgumentException("Invalid field to update: " + field);
			}
		});
	}

	public void deleteInsuranceById(Long id) {
		if (!insuranceRepository.existsById(id)) {
			throw new EntityNotFoundException("Insurance not found with id: " + id);
		}
		insuranceRepository.deleteById(id);
	}
}
