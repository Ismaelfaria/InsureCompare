package com.project.InsureCompare.application.service.interfaces;

import java.util.List;
import java.util.Map;

import com.project.InsureCompare.application.dto.InsuranceDTO;
import com.project.InsureCompare.domain.entity.Insurance;

public interface InsuranceService {

	List<InsuranceDTO> getAllInsurancesOrderedByPrice();
	InsuranceDTO getInsuranceById(Long id);
	Insurance saveInsurance(InsuranceDTO insuranceDTO);
	InsuranceDTO updateInsurance(Long id, Map<String, Object> updateRequest);
	void updateInsuranceFields(Insurance existingInsurance, Map<String, Object> updateRequest);
	void deleteInsuranceById(Long id);
}
