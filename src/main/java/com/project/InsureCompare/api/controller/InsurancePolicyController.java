package com.project.InsureCompare.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.InsureCompare.application.dto.ClientDTO;
import com.project.InsureCompare.application.dto.InsurancePolicyDTO;
import com.project.InsureCompare.application.service.ClientService;
import com.project.InsureCompare.application.service.InsurancePolicyService;
import com.project.InsureCompare.domain.entity.InsurancePolicy;

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
@RequestMapping("/insurance-policies")
public class InsurancePolicyController {

	@Autowired
	private InsurancePolicyService insurancePolicyService;
	
	@Autowired
	private ClientService clientService;

	@PostMapping
	public ResponseEntity<InsurancePolicyDTO> savePolicy(@RequestBody InsurancePolicyDTO policyDTO) {
		try {
			InsurancePolicy savedPolicy = insurancePolicyService.savePolicy(policyDTO);
			InsurancePolicyDTO savedPolicyDTO = insurancePolicyService.findInsurancePolicyById(savedPolicy.getId())
					.orElse(null);
			return new ResponseEntity<>(savedPolicyDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<InsurancePolicyDTO> findInsurancePolicyById(@PathVariable Long id) {
		try {
			Optional<InsurancePolicyDTO> policyDTO = insurancePolicyService.findInsurancePolicyById(id);
			return policyDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<InsurancePolicyDTO>> findAllInsurancePolicies() {
		try {
			List<InsurancePolicyDTO> policies = insurancePolicyService.findAllInsurancePolicy();
			return ResponseEntity.ok(policies);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/client/{clientId}/status/{status}")
    public ResponseEntity<List<InsurancePolicyDTO>> findPoliciesByClientAndStatus(
            @PathVariable Long clientId,
            @PathVariable String status) {
        
        Optional<ClientDTO> client = clientService.findClientById(clientId);
        
        if (client.isEmpty()) {
            return ResponseEntity.notFound().build(); 
        }

        List<InsurancePolicyDTO> policies = insurancePolicyService.findPoliciesByClientAndStatus(clientId, status);
        return ResponseEntity.ok(policies);
}

	@PutMapping("/{id}")
	public ResponseEntity<InsurancePolicyDTO> updateInsurancePolicy(@PathVariable Long id,
			@RequestBody Map<String, Object> updateRequest) {
		try {
			InsurancePolicyDTO updatedPolicy = insurancePolicyService.updateInsurancePolicy(id, updateRequest);
			return ResponseEntity.ok(updatedPolicy);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePolicyById(@PathVariable Long id) {
		try {
			insurancePolicyService.deletePolicyById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
