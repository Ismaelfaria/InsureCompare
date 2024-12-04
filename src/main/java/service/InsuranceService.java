package service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Insurance;
import jakarta.persistence.EntityNotFoundException;
import repository.InsuranceRepository;

@Service
public class InsuranceService {

	@Autowired
	private InsuranceRepository insuranceRepository;

    public List<Insurance> getAllInsurancesOrderedByPrice() {
        return insuranceRepository.findAllSortedByPrecoBaseAsc();
    }

    public Insurance getInsuranceById(Long id) {
        return insuranceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + id));
    }

    public Insurance saveInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }
    
    public Insurance updateInsurance(Long id, Map<String, Object> updateRequest) {
    	
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
        return insuranceRepository.save(existingInsurance);
    }


    public void deleteInsuranceById(Long id) {
        if (!insuranceRepository.existsById(id)) {
            throw new EntityNotFoundException("Insurance not found with id: " + id);
        }
        insuranceRepository.deleteById(id);
    }
}
