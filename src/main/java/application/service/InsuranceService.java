package application.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dto.InsuranceDTO;
import application.mappers.InsuranceMapper;
import domain.entity.Insurance;
import infra.repository.InsuranceRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class InsuranceService {

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
