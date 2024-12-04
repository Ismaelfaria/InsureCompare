package service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Client;
import domain.Insurance;
import domain.InsurancePolicy;
import jakarta.persistence.EntityNotFoundException;
import repository.InsurancePolicyRepository;

@Service
public class InsurancePolicyService {

	@Autowired
	private InsurancePolicyRepository insurancePolicyRepository;

    public List<InsurancePolicy> getPoliciesByClientAndStatus(Client client, String status) {
        return insurancePolicyRepository.findByCustomerAndStatus(client, status);
    }

    public Optional<InsurancePolicy> findInsurancePolicyById(Long id) {
        return insurancePolicyRepository.findById(id);
    }
    
    public List<InsurancePolicy> findAllInsurancePolicy(Long id) {
        List<InsurancePolicy> allInsurancePolicy =  insurancePolicyRepository.findAll();
        return allInsurancePolicy;
    }
    
    public InsurancePolicy savePolicy(InsurancePolicy policy) {
        return insurancePolicyRepository.save(policy);
    }
    
    public InsurancePolicy updateInsurancePolicy(Long id, Map<String, Object> updateRequest) {

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

        return insurancePolicyRepository.save(existingPolicy);
        }

    public void deletePolicyById(Long id) {
        if (!insurancePolicyRepository.existsById(id)) {
            throw new EntityNotFoundException("Insurance policy not found with id: " + id);
        }
        insurancePolicyRepository.deleteById(id);
    }
	
}
