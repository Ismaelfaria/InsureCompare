package com.project.InsureCompare.api.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.InsureCompare.application.dto.InsuranceDTO;
import com.project.InsureCompare.application.service.InsuranceService;
import com.project.InsureCompare.domain.entity.Insurance;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/InsuranceController")
public class InsuranceController {

	@Autowired
	private InsuranceService insuranceService;

	@PostMapping
	public ResponseEntity<Insurance> saveInsurance(@RequestBody InsuranceDTO insuranceDTO) {
		try {
			Insurance savedInsurance = insuranceService.saveInsurance(insuranceDTO);
			return new ResponseEntity<>(savedInsurance, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/insurances/{id}")
	public ResponseEntity<InsuranceDTO> findInsuranceById(@PathVariable Long id) {
		try {
			Optional<InsuranceDTO> insuranceDTO = Optional.ofNullable(insuranceService.getInsuranceById(id));
			return insuranceDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping
	public ResponseEntity<List<InsuranceDTO>> findAllInsurancesOrderedByPrice() {
		try {
			List<InsuranceDTO> insurances = insuranceService.getAllInsurancesOrderedByPrice();
			return ResponseEntity.ok(insurances);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<InsuranceDTO> updateInsurance(@PathVariable Long id,
			@RequestBody Map<String, Object> updateRequest) {
		try {
			InsuranceDTO updatedInsurance = insuranceService.updateInsurance(id, updateRequest);
			return ResponseEntity.ok(updatedInsurance);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteInsuranceById(@PathVariable Long id) {
		try {
			insuranceService.deleteInsuranceById(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}