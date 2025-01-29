package app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.InsureCompare.application.dto.InsurancePolicyDTO;
import com.project.InsureCompare.application.mappers.InsurancePolicyMapper;
import com.project.InsureCompare.application.messaging.PolicyApprovalService;
import com.project.InsureCompare.application.messaging.dto.PolicyApprovalMessageRequest;
import com.project.InsureCompare.application.service.InsurancePolicyService;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.domain.entity.InsurancePolicy;
import com.project.InsureCompare.infra.repository.ClientRepository;
import com.project.InsureCompare.infra.repository.InsurancePolicyRepository;

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

	@Mock
	private PolicyApprovalService policyApprovalService;

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private InsurancePolicyService insurancePolicyService;

	static Client client;
	static InsurancePolicy insurancePolicy;
	static InsurancePolicyDTO insurancePolicyDTO;
	static Long invalidId;

	@BeforeAll
	public static void setUpEntities() {
		client = new Client(1L, "John Doe", "john.doe@gmail.com", "123456789", "123 Main St");
		Insurance insurance = new Insurance(1L, "Health Insurance", 500.0);
		insurancePolicy = new InsurancePolicy(1L, client, insurance, "12345", "ACTIVE");
		insurancePolicyDTO = new InsurancePolicyDTO(insurancePolicy.getId(), client.getId(),
				insurancePolicy.getPolicyInsuranceNumber(), insurancePolicy.getStatus());
		invalidId = 999L;
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

		PolicyApprovalMessageRequest expectedRequest = new PolicyApprovalMessageRequest();
		expectedRequest.setPolicyId(insurancePolicy.getId());
		expectedRequest.setPolicyHolderNumber(insurancePolicy.getPolicyInsuranceNumber());
		expectedRequest.setPolicyStatus(insurancePolicy.getStatus());

		doNothing().when(policyApprovalService).sendApprovalRequest(Mockito.any(PolicyApprovalMessageRequest.class));

		InsurancePolicy result = insurancePolicyService.savePolicy(insurancePolicyDTO);

		assertEquals(insurancePolicy, result);
		assertEquals(insurancePolicy.getId(), result.getId());
		assertEquals(insurancePolicy.getInsurance(), result.getInsurance());
		assertEquals(insurancePolicy.getPolicyInsuranceNumber(), result.getPolicyInsuranceNumber());
		assertEquals(insurancePolicy.getStatus(), result.getStatus());

		ArgumentCaptor<PolicyApprovalMessageRequest> captor = ArgumentCaptor
				.forClass(PolicyApprovalMessageRequest.class);
		verify(policyApprovalService).sendApprovalRequest(captor.capture());

		PolicyApprovalMessageRequest capturedRequest = captor.getValue();
		assertEquals(expectedRequest.getPolicyId(), capturedRequest.getPolicyId());
		assertEquals(expectedRequest.getPolicyHolderNumber(), capturedRequest.getPolicyHolderNumber());
		assertEquals(expectedRequest.getPolicyStatus(), capturedRequest.getPolicyStatus());
	}

	@Test
	void testSavePolicyWithInvalidData() {
		InsurancePolicy invalidPolicy = new InsurancePolicy();
		invalidPolicy.setId(null);
		invalidPolicy.setPolicyInsuranceNumber(null);
		invalidPolicy.setStatus("INVALID_STATUS");

		InsurancePolicyDTO invalidPolicyDTO = new InsurancePolicyDTO(invalidPolicy.getId(), client.getId(),
				invalidPolicy.getPolicyInsuranceNumber(), invalidPolicy.getStatus());

		assertThrows(NullPointerException.class, () -> {
			insurancePolicyService.savePolicy(invalidPolicyDTO);
		});
	}

	@Test
	void testSavePolicyWhenRepositoryFails() {
		lenient().when(insurancePolicyRepository.save(any(InsurancePolicy.class)))
				.thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> {
			insurancePolicyService.savePolicy(insurancePolicyDTO);
		});
	}

	@Test
	void testFindPoliciesByClientAndStatusWithValidClientAndStatus() {
		client.setId(client.getId());

		InsurancePolicy insurancePolicy = new InsurancePolicy();
		insurancePolicy.setClient(client);
		insurancePolicy.setStatus(insurancePolicy.getStatus());

		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(insurancePolicyRepository.findByCustomerAndStatus(Optional.of(client), insurancePolicy.getStatus()))
				.thenReturn(List.of(insurancePolicy));
		when(insurancePolicyMapper.toDTO(insurancePolicy)).thenReturn(insurancePolicyDTO);

		List<InsurancePolicyDTO> result = insurancePolicyService.findPoliciesByClientAndStatus(client.getId(), insurancePolicy.getStatus());

		assertNotNull(result);
		assertEquals(1, result.size());

		verify(clientRepository).findById(client.getId());
		verify(insurancePolicyRepository).findByCustomerAndStatus(Optional.of(client), insurancePolicy.getStatus());
		verify(insurancePolicyMapper, times(1)).toDTO(any(InsurancePolicy.class));
	}

	@Test
	void testFindPoliciesByClientAndStatusWithInvalidClient() {
		when(insurancePolicyRepository.findByCustomerAndStatus(eq(Optional.empty()), eq(insurancePolicy.getStatus())))
				.thenThrow(new IllegalArgumentException("Customer cannot be null"));
		assertThrows(IllegalArgumentException.class, () -> {
			insurancePolicyService.findPoliciesByClientAndStatus(null, insurancePolicy.getStatus());
		});
	}

	@Test
	void testFindPoliciesByClientAndStatusWithNonExistentStatus() {
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(insurancePolicyRepository.findByCustomerAndStatus(Optional.of(client), null))
				.thenReturn(Collections.emptyList());

		var result = insurancePolicyService.findPoliciesByClientAndStatus(client.getId(), null);

		assertTrue(result.isEmpty(), "The result should be an empty list for a non-existent status");

		verify(clientRepository).findById(client.getId());
		verify(insurancePolicyRepository).findByCustomerAndStatus(Optional.of(client), null);
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
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("status", "NEW_STATUS");

		when(insurancePolicyRepository.findById(invalidId)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> {
			insurancePolicyService.updateInsurancePolicy(invalidId, updateRequest);
		});

		verify(insurancePolicyRepository, times(1)).findById(invalidId);
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
		when(insurancePolicyRepository.existsById(invalidId)).thenReturn(false);

		assertThrows(EntityNotFoundException.class, () -> {
			insurancePolicyService.deletePolicyById(invalidId);
		});

		verify(insurancePolicyRepository, times(1)).existsById(invalidId);
		verify(insurancePolicyRepository, never()).deleteById(invalidId);
	}
}
