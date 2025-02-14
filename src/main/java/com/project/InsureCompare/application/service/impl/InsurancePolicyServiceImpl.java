package com.project.InsureCompare.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.InsurancePolicyDTO;
import com.project.InsureCompare.application.mappers.InsurancePolicyMapper;
import com.project.InsureCompare.application.messaging.PolicyApprovalService;
import com.project.InsureCompare.application.messaging.dto.PolicyApprovalMessageRequest;
import com.project.InsureCompare.application.service.interfaces.InsurancePolicyService;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.InsurancePolicy;
import com.project.InsureCompare.infra.repository.ClientRepository;
import com.project.InsureCompare.infra.repository.InsurancePolicyRepository;
import com.project.InsureCompare.util.EntityUpdater;

import jakarta.persistence.EntityNotFoundException;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PolicyApprovalService policyApprovalService;

	@Autowired
	private InsurancePolicyMapper insurancePolicyMapper;

	@Autowired
	private EntityUpdater entityUpdater;

	public List<InsurancePolicyDTO> findPoliciesByClientAndStatus(Long clientId, String status) {
		Optional<Client> clientByPolicies = clientRepository.findById(clientId);

		return insurancePolicyRepository.findByCustomerAndStatus(clientByPolicies, status).stream()
				.map(insurancePolicyMapper::toDTO).toList();
	}

	public Optional<InsurancePolicyDTO> findInsurancePolicyById(Long id) {
		Optional<InsurancePolicy> insurancePolicyOptional = insurancePolicyRepository.findById(id);

		if (insurancePolicyOptional.isEmpty()) {
			throw new EntityNotFoundException("Insurance Policy not found with id: " + id);
		}

		return insurancePolicyOptional.map(insurancePolicyMapper::toDTO);
	}

	public List<InsurancePolicyDTO> findAllInsurancePolicy() {
		return insurancePolicyRepository.findAll().stream().map(insurancePolicyMapper::toDTO).toList();
	}

	public InsurancePolicy savePolicy(InsurancePolicyDTO policyDTO) {
		InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(policyDTO);
		PolicyApprovalMessageRequest request = createApprovalRequest(insurancePolicy);

		policyApprovalService.sendApprovalRequest(request);

		return insurancePolicyRepository.save(insurancePolicy);
	}

	public PolicyApprovalMessageRequest createApprovalRequest(InsurancePolicy insurancePolicy) {
		PolicyApprovalMessageRequest request = new PolicyApprovalMessageRequest();
		request.setClientId(insurancePolicy.getClient().getId());
		request.setInsuranceId(insurancePolicy.getInsurance().getId());
		request.setPolicyHolderNumber(insurancePolicy.getPolicyInsuranceNumber());
		request.setPolicyStatus(insurancePolicy.getStatus());

		return request;
	}

	public InsurancePolicyDTO updateInsurancePolicy(Long id, Map<String, Object> updateRequest) {
		Optional<InsurancePolicy> existingInsurancePolicyOption = insurancePolicyRepository.findById(id);

		InsurancePolicy existingInsurancePolicy = existingInsurancePolicyOption.get();
		entityUpdater.updateEntityFields(existingInsurancePolicy, updateRequest);
		InsurancePolicy insurancePolicyUpdate = insurancePolicyRepository.save(existingInsurancePolicy);

		return insurancePolicyMapper.toDTO(insurancePolicyUpdate);
	}

	public void deletePolicyById(Long id) {
		if (!insurancePolicyRepository.existsById(id)) {
			throw new EntityNotFoundException("Insurance policy not found with id: " + id);
		}
		insurancePolicyRepository.deleteById(id);
	}

}
