package com.project.InsureCompare.application.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.InsureCompare.application.dto.InsurancePolicyDTO;
import com.project.InsureCompare.application.messaging.dto.PolicyApprovalMessageRequest;
import com.project.InsureCompare.domain.entity.InsurancePolicy;

public interface InsurancePolicyService {

	List<InsurancePolicyDTO> findPoliciesByClientAndStatus(Long clientId, String status);
	Optional<InsurancePolicyDTO> findInsurancePolicyById(Long id);
	List<InsurancePolicyDTO> findAllInsurancePolicy();
	InsurancePolicy savePolicy(InsurancePolicyDTO policyDTO);
	PolicyApprovalMessageRequest createApprovalRequest(InsurancePolicy insurancePolicy);
	InsurancePolicyDTO updateInsurancePolicy(Long id, Map<String, Object> updateRequest);
	void deletePolicyById(Long id);
}
