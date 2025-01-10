package app.mappers;

import org.springframework.stereotype.Component;

import app.dto.InsurancePolicyDTO;
import domain.entity.Client;
import domain.entity.Insurance;
import domain.entity.InsurancePolicy;
import infra.repository.ClientRepository;
import infra.repository.InsuranceRepository;

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

		Client client = clientRepository.findById(dto.customerId())
				.orElseThrow(() -> new IllegalArgumentException("Client with ID " + dto.customerId() + " not found"));

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
