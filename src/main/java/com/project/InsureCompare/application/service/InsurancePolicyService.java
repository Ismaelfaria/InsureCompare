package com.project.InsureCompare.application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.InsurancePolicyDTO;
import com.project.InsureCompare.application.mappers.InsurancePolicyMapper;
import com.project.InsureCompare.application.messaging.PolicyApprovalService;
import com.project.InsureCompare.application.messaging.dto.PolicyApprovalMessageRequest;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.domain.entity.InsurancePolicy;
import com.project.InsureCompare.infra.repository.InsurancePolicyRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InsurancePolicyService {

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private PolicyApprovalService policyApprovalService;

	@Autowired
	private InsurancePolicyMapper insurancePolicyMapper;

	public Optional<InsurancePolicyDTO> findInsurancePolicyById(Long id) {
		return insurancePolicyRepository.findById(id).map(insurancePolicyMapper::toDTO);
	}

	public List<InsurancePolicyDTO> findAllInsurancePolicy() {
		return insurancePolicyRepository.findAll().stream().map(insurancePolicyMapper::toDTO).toList();
	}

	public InsurancePolicy savePolicy(InsurancePolicyDTO policyDTO) {
		InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(policyDTO);
		PolicyApprovalMessageRequest request = createApprovalRequest(insurancePolicy);

		policyApprovalService.sendApprovalRequest(request);

		System.out.println("Mensagem enviada para a fila de aprovação.");

		return insurancePolicyRepository.save(insurancePolicy);
	}

	private PolicyApprovalMessageRequest createApprovalRequest (InsurancePolicy insurancePolicy) {
		PolicyApprovalMessageRequest request = new PolicyApprovalMessageRequest();
		request.setPolicyId(insurancePolicy.getId());
		request.setPolicyHolderNumber(insurancePolicy.getPolicyInsuranceNumber());
		request.setPolicyStatus(insurancePolicy.getStatus());
		
		return request;
	}
	
	public InsurancePolicyDTO updateInsurancePolicy(Long id, Map<String, Object> updateRequest) {

		InsurancePolicy existingInsurancePolicy = insurancePolicyRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Insurance policy not found with id: " + id));

		updateInsurancePolicyFields(existingInsurancePolicy, updateRequest);

		InsurancePolicy insurancePolicyUpdate = insurancePolicyRepository.save(existingInsurancePolicy);

		return insurancePolicyMapper.toDTO(insurancePolicyUpdate);
	}

	private void updateInsurancePolicyFields(InsurancePolicy existingInsurancePolicy, Map<String, Object> updateRequest) {
		updateRequest.forEach((field, newValue) -> {
			switch (field.toLowerCase()) {
			case "status":
				existingInsurancePolicy.setStatus((String) newValue);
				break;
			case "policyNumber":
				existingInsurancePolicy.setPolicyInsuranceNumber((String) newValue);
				break;
			case "insurance":
				existingInsurancePolicy.setInsurance((Insurance) newValue);
				break;
			case "client":
				existingInsurancePolicy.setClient((Client) newValue);
				break;
			default:
				throw new IllegalArgumentException("Invalid field to update: " + field);
			}
		});
	}

	public void deletePolicyById(Long id) {
		if (!insurancePolicyRepository.existsById(id)) {
			throw new EntityNotFoundException("Insurance policy not found with id: " + id);
		}
		insurancePolicyRepository.deleteById(id);
	}

}
