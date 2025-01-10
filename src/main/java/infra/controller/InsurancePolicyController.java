package infra.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dto.InsurancePolicyDTO;
import app.service.InsurancePolicyService;
import domain.entity.InsurancePolicy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/InsurancePolicy")
public class InsurancePolicyController {

	@Autowired
	private InsurancePolicyService insurancePolicyService;
	
    @PostMapping("/policies")
    public ResponseEntity<InsurancePolicyDTO> savePolicy(@RequestBody InsurancePolicyDTO policyDTO) {
        InsurancePolicy savedPolicy = insurancePolicyService.savePolicy(policyDTO);
        InsurancePolicyDTO savedPolicyDTO = insurancePolicyService.findInsurancePolicyById(savedPolicy.getId()).orElse(null);
        return new ResponseEntity<>(savedPolicyDTO, HttpStatus.CREATED);
    }

    @GetMapping("/policies/{id}")
    public ResponseEntity<InsurancePolicyDTO> findInsurancePolicyById(@PathVariable Long id) {
        Optional<InsurancePolicyDTO> policyDTO = insurancePolicyService.findInsurancePolicyById(id);
        return policyDTO.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/policies")
    public ResponseEntity<List<InsurancePolicyDTO>> findAllInsurancePolicies() {
        List<InsurancePolicyDTO> policies = insurancePolicyService.findAllInsurancePolicy();
        return ResponseEntity.ok(policies);
    }

    @PutMapping("/policies/{id}")
    public ResponseEntity<InsurancePolicyDTO> updateInsurancePolicy(@PathVariable Long id, @RequestBody Map<String, Object> updateRequest) {
        InsurancePolicyDTO updatedPolicy = insurancePolicyService.updateInsurancePolicy(id, updateRequest);
        return ResponseEntity.ok(updatedPolicy);
    }

    @DeleteMapping("/policies/{id}")
    public ResponseEntity<Void> deletePolicyById(@PathVariable Long id) {
        insurancePolicyService.deletePolicyById(id);
        return ResponseEntity.noContent().build();
    }
	
}
