package app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.dto.InsurancePolicyDTO;
import app.mappers.InsurancePolicyMapper;
import domain.entity.Client;
import domain.entity.Insurance;
import domain.entity.InsurancePolicy;
import infra.repository.InsurancePolicyRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {

	@Mock
	private InsurancePolicyRepository insurancePolicyRepository;

	@Mock
	private InsurancePolicyMapper insurancePolicyMapper;

	@InjectMocks
	private InsurancePolicyService insurancePolicyService;

	static Client client;
	static Insurance insurance;
	static InsurancePolicy insurancePolicy;
	static InsurancePolicyDTO insurancePolicyDTO;

	@BeforeAll
	public static void setUpEntities() {
		client = new Client(1L, "John Doe", "john.doe@gmail.com", "123456789", "123 Main St");
		insurance = new Insurance(1L, "Health Insurance", 500.0);
		insurancePolicy = new InsurancePolicy(1L, client, insurance, "12345", "ACTIVE");
		insurancePolicyDTO = new InsurancePolicyDTO(insurancePolicy.getId(), client.getId(),
	    insurancePolicy.getPolicyInsuranceNumber(), insurancePolicy.getStatus());
	}

	@Test
	void testFindPoliciesByClientAndStatusWithValidClientAndStatus() {
		String activeStatus = "ACTIVE";

		when(insurancePolicyRepository.findByCustomerAndStatus(client, activeStatus))
				.thenReturn(List.of(insurancePolicy));
		when(insurancePolicyMapper.toDTO(insurancePolicy)).thenReturn(insurancePolicyDTO);

		List<InsurancePolicyDTO> result = insurancePolicyService.findPoliciesByClientAndStatus(client, activeStatus);

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(insurancePolicyRepository).findByCustomerAndStatus(client, activeStatus);
		verify(insurancePolicyMapper, times(1)).toDTO(any(InsurancePolicy.class));
	}

	@Test
	void testFindPoliciesByClientAndStatusWithInvalidClient() {
		String activeStatus = "ACTIVE";

		when(insurancePolicyRepository.findByCustomerAndStatus(null, activeStatus))
				.thenThrow(new IllegalArgumentException("Customer cannot be null"));

		assertThrows(IllegalArgumentException.class, () -> {
			insurancePolicyService.findPoliciesByClientAndStatus(null, activeStatus);
		});
	}

	@Test
	void testFindPoliciesByClientAndStatusWithNonExistentStatus() {

		when(insurancePolicyRepository.findByCustomerAndStatus(client, null)).thenReturn(Collections.emptyList());

		var result = insurancePolicyService.findPoliciesByClientAndStatus(client, null);

		assertTrue(result.isEmpty(), "The result should be an empty list for a non-existent status");
	}

	@Test
	void testFindInsurancePolicyByIdWithValidId() {
		when(insurancePolicyRepository.findById(insurancePolicy.getId())).thenReturn(Optional.of(insurancePolicy));
		when(insurancePolicyMapper.toDTO(insurancePolicy)).thenReturn(insurancePolicyDTO);

		Optional<InsurancePolicyDTO> result = insurancePolicyService.findInsurancePolicyById(insurancePolicy.getId());

		assertTrue(result.isPresent());
		assertEquals(insurancePolicyDTO, result.get());
		assertEquals(insurancePolicyDTO.clientId(), result.get().clientId());
		assertEquals(insurancePolicyDTO.insuranceId(), result.get().insuranceId());
		assertEquals(insurancePolicyDTO.status(), result.get().status());
		assertEquals(insurancePolicyDTO.policyNumber(), result.get().policyNumber());
	}

	@Test
	void testFindInsurancePolicyByIdWithInvalidId() {
		when(insurancePolicyRepository.findById(null)).thenReturn(Optional.empty());

		Optional<InsurancePolicyDTO> result = insurancePolicyService.findInsurancePolicyById(null);

		assertTrue(result.isEmpty());
	}

	@Test
	void testFindInsurancePolicyByIdWhenPolicyDoesNotExist() {
		Long invalidId = 999L;

		when(insurancePolicyRepository.findById(invalidId)).thenReturn(Optional.empty());

		Optional<InsurancePolicyDTO> result = insurancePolicyService.findInsurancePolicyById(invalidId);

		assertTrue(result.isEmpty());
	}

	@Test
	void testFindAllInsurancePolicyWhenPoliciesExist() {
		List<InsurancePolicy> policies = List.of(insurancePolicy);

		when(insurancePolicyRepository.findAll()).thenReturn(policies);
		when(insurancePolicyMapper.toDTO(insurancePolicy)).thenReturn(insurancePolicyDTO);

		List<InsurancePolicyDTO> result = insurancePolicyService.findAllInsurancePolicy();

		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(insurancePolicyDTO, result.get(0));
	}

	@Test
	void testFindAllInsurancePolicyWhenNoPoliciesExist() {
		when(insurancePolicyRepository.findAll()).thenReturn(Collections.emptyList());

		List<InsurancePolicyDTO> result = insurancePolicyService.findAllInsurancePolicy();

		assertTrue(result.isEmpty());
	}

	@Test
	void testSavePolicyWithValidData() {
		when(insurancePolicyRepository.save(insurancePolicy)).thenReturn(insurancePolicy);
		when(insurancePolicyMapper.toEntity(insurancePolicyDTO)).thenReturn(insurancePolicy);

		InsurancePolicy result = insurancePolicyService.savePolicy(insurancePolicyDTO);

		assertEquals(insurancePolicy, result);
		assertEquals(insurancePolicy.getId(), result.getId());
		assertEquals(insurancePolicy.getInsurance(), result.getInsurance());
		assertEquals(insurancePolicy.getPolicyInsuranceNumber(), result.getPolicyInsuranceNumber());
		assertEquals(insurancePolicy.getStatus(), result.getStatus());
	}

	@Test
	void testSavePolicyWithInvalidData() {
		InsurancePolicy invalidPolicy = new InsurancePolicy();
		invalidPolicy.setId(null);
		invalidPolicy.setPolicyInsuranceNumber(null);
		invalidPolicy.setStatus("INVALID_STATUS");

		when(insurancePolicyRepository.save(null)).thenThrow(new IllegalArgumentException("Invalid policy data"));

		InsurancePolicyDTO invalidPolicyDTO = new InsurancePolicyDTO(invalidPolicy.getId(), client.getId(),
				invalidPolicy.getPolicyInsuranceNumber(), invalidPolicy.getStatus());

		assertThrows(IllegalArgumentException.class, () -> {
			insurancePolicyService.savePolicy(invalidPolicyDTO);
		});
	}

	@Test
	void testSavePolicyWhenRepositoryFails() {
		when(insurancePolicyRepository.save(any(InsurancePolicy.class)))
				.thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> {
			insurancePolicyService.savePolicy(insurancePolicyDTO);
		});
	}

	@Test
	void testUpdateInsurancePolicyWithValidFields() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("status", "NEW_STATUS");

		when(insurancePolicyRepository.findById(insurancePolicy.getId())).thenReturn(Optional.of(insurancePolicy));
		when(insurancePolicyRepository.save(insurancePolicy)).thenReturn(insurancePolicy);
		when(insurancePolicyMapper.toDTO(insurancePolicy)).thenReturn(insurancePolicyDTO);

		InsurancePolicyDTO result = insurancePolicyService.updateInsurancePolicy(insurancePolicy.getId(),
				updateRequest);

		assertNotNull(result);
		assertEquals("NEW_STATUS", insurancePolicy.getStatus());

		verify(insurancePolicyRepository, times(1)).findById(insurancePolicy.getId());
		verify(insurancePolicyRepository, times(1)).save(insurancePolicy);
		verify(insurancePolicyMapper, times(1)).toDTO(insurancePolicy);
	}

	@Test
	void testUpdateInsurancePolicyWithInvalidFields() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("invalidField", "someValue");

		when(insurancePolicyRepository.findById(insurancePolicy.getId())).thenReturn(Optional.of(insurancePolicy));

		assertThrows(IllegalArgumentException.class, () -> {
			insurancePolicyService.updateInsurancePolicy(insurancePolicy.getId(), updateRequest);
		});

		verify(insurancePolicyRepository, times(1)).findById(insurancePolicy.getId());
		verify(insurancePolicyRepository, never()).save(any());
		verify(insurancePolicyMapper, never()).toDTO(any());
	}

	@Test
	void testUpdateInsurancePolicyWhenPolicyDoesNotExist() {
		Long nonExistentPolicyId = 999L;
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("status", "NEW_STATUS");

		when(insurancePolicyRepository.findById(nonExistentPolicyId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			insurancePolicyService.updateInsurancePolicy(nonExistentPolicyId, updateRequest);
		});

		verify(insurancePolicyRepository, times(1)).findById(nonExistentPolicyId);
		verify(insurancePolicyRepository, never()).save(any());
		verify(insurancePolicyMapper, never()).toDTO(any());
	}

	@Test
	void testDeletePolicyByIdWithValidId() {
		when(insurancePolicyRepository.existsById(insurancePolicy.getId())).thenReturn(true);
		doNothing().when(insurancePolicyRepository).deleteById(insurancePolicy.getId());

		insurancePolicyService.deletePolicyById(insurancePolicy.getId());

		verify(insurancePolicyRepository, times(1)).existsById(insurancePolicy.getId());
		verify(insurancePolicyRepository, times(1)).deleteById(insurancePolicy.getId());
	}

	@Test
	void testDeletePolicyByIdWhenPolicyDoesNotExist() {
		Long nonExistentPolicyId = 999L;
		when(insurancePolicyRepository.existsById(nonExistentPolicyId)).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> {
			insurancePolicyService.deletePolicyById(nonExistentPolicyId);
		});

		verify(insurancePolicyRepository, times(1)).existsById(nonExistentPolicyId);
		verify(insurancePolicyRepository, never()).deleteById(nonExistentPolicyId);
	}
}
