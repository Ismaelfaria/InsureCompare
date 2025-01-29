package com.project.InsureCompare.application.mappers;

import org.springframework.stereotype.Component;

import com.project.InsureCompare.application.dto.InsurancePolicyDTO;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.domain.entity.InsurancePolicy;
import com.project.InsureCompare.infra.repository.ClientRepository;
import com.project.InsureCompare.infra.repository.InsuranceRepository;

@Component
public class InsurancePolicyMapper {

	private final ClientRepository clientRepository;
	private final InsuranceRepository insuranceRepository;

	public InsurancePolicyMapper(ClientRepository clientRepository, InsuranceRepository insuranceRepository) {
		this.clientRepository = clientRepository;
		this.insuranceRepository = insuranceRepository;
	}

	public InsurancePolicy toEntity(InsurancePolicyDTO dto) {
		InsurancePolicy insurancePolicy = new InsurancePolicy();

		Client client = clientRepository.findById(dto.clientId())
				.orElseThrow(() -> new IllegalArgumentException("Client with ID " + dto.clientId() + " not found"));

		Insurance insurance = insuranceRepository.findById(dto.insuranceId()).orElseThrow(
				() -> new IllegalArgumentException("Insurance with ID " + dto.insuranceId() + " not found"));

		insurancePolicy.setClient(client);
		insurancePolicy.setInsurance(insurance);
		insurancePolicy.setPolicyInsuranceNumber(dto.policyNumber());
		insurancePolicy.setStatus(dto.status());

		return insurancePolicy;
	}

	public InsurancePolicyDTO toDTO(InsurancePolicy insurancePolicy) {
		return new InsurancePolicyDTO(insurancePolicy.getClient().getId(), insurancePolicy.getInsurance().getId(),
				insurancePolicy.getPolicyInsuranceNumber(), insurancePolicy.getStatus());
	}

}
