package api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.dto.InsurancePolicyDTO;
import application.service.InsurancePolicyService;
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
		try {
			InsurancePolicy savedPolicy = insurancePolicyService.savePolicy(policyDTO);
			InsurancePolicyDTO savedPolicyDTO = insurancePolicyService.findInsurancePolicyById(savedPolicy.getId())
					.orElse(null);
			return new ResponseEntity<>(savedPolicyDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/policies/{id}")
	public ResponseEntity<InsurancePolicyDTO> findInsurancePolicyById(@PathVariable Long id) {
		try {
			Optional<InsurancePolicyDTO> policyDTO = insurancePolicyService.findInsurancePolicyById(id);
			return policyDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/policies")
	public ResponseEntity<List<InsurancePolicyDTO>> findAllInsurancePolicies() {
		try {
			List<InsurancePolicyDTO> policies = insurancePolicyService.findAllInsurancePolicy();
			return ResponseEntity.ok(policies);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/policies/{id}")
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

	@DeleteMapping("/policies/{id}")
	public ResponseEntity<Void> deletePolicyById(@PathVariable Long id) {
		try {
			insurancePolicyService.deletePolicyById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
