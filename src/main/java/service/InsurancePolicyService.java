package service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Mappers.InsurancePolicyMapper;
import domain.Client;
import domain.Insurance;
import domain.InsurancePolicy;
import dto.InsurancePolicyDTO;
import jakarta.persistence.EntityNotFoundException;
import repository.InsurancePolicyRepository;

@Service
public class InsurancePolicyService {

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;
	
	@Autowired
	private InsurancePolicyMapper insurancePolicyMapper;


    public List<InsurancePolicyDTO> findPoliciesByClientAndStatus(Client client, String status) {
        return insurancePolicyRepository.findByCustomerAndStatus(client, status)
        		.stream()
        		.map(insurancePolicyMapper::toDTO)
        		.toList();
    }

    public Optional<InsurancePolicyDTO> findInsurancePolicyById(Long id) {
        return insurancePolicyRepository.findById(id)
        		.map(insurancePolicyMapper::toDTO);
    }
    
    public List<InsurancePolicyDTO> findAllInsurancePolicy() {
    	return insurancePolicyRepository.findAll()
        		.stream()
        		.map(insurancePolicyMapper::toDTO)
        		.toList();
    }
    
    public InsurancePolicy savePolicy(InsurancePolicyDTO policyDTO) {
    	
    	InsurancePolicy insurancePolicy = insurancePolicyMapper.toEntity(policyDTO);
    	
        return insurancePolicyRepository.save(insurancePolicy);
    }
    
    public InsurancePolicyDTO updateInsurancePolicy(Long id, Map<String, Object> updateRequest) {

		InsurancePolicy existingPolicy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance policy not found with id: " + id));

        updateRequest.forEach((field, newValue) -> {
            switch (field.toLowerCase()) {
                case "status":
                    existingPolicy.setStatus((String) newValue);
                    break;
                case "policyNumber":
                    existingPolicy.setPolicyInsuranceNumber((String) newValue);
                    break;
                case "insurance":
                    existingPolicy.setInsurance((Insurance) newValue);
                    break;
                case "client":
                    existingPolicy.setClient((Client) newValue);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field to update: " + field);
            }
        });
        
        InsurancePolicy insurancePolicyUpdate = insurancePolicyRepository.save(existingPolicy);
        
        return insurancePolicyMapper.toDTO(insurancePolicyUpdate);
        }

    public void deletePolicyById(Long id) {
        if (!insurancePolicyRepository.existsById(id)) {
            throw new EntityNotFoundException("Insurance policy not found with id: " + id);
        }
        insurancePolicyRepository.deleteById(id);
    }
	
}
