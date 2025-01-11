package app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import app.dto.ClientDTO;
import app.dto.InsurancePolicyDTO;
import app.mappers.ClientMapper;
import app.mappers.InsurancePolicyMapper;
import app.service.ClientService;
import domain.entity.Client;
import domain.entity.Insurance;
import domain.entity.InsurancePolicy;
import infra.repository.ClientRepository;
import infra.repository.InsurancePolicyRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
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

	@BeforeAll
	public static void setUpEntities() {
		client = new Client(1L, "John Doe", "john.doe@gmail.com", "123456789", "123 Main St");
		insurance = new Insurance(1L, "Health Insurance", 500.0);
		insurancePolicy = new InsurancePolicy(1L, client, insurance, "12345", "ACTIVE");
	}

	@Test
	void testFindPoliciesByClientAndStatusWithValidClientAndStatus() {
		String activeStatus = "ACTIVE";

		InsurancePolicy insurancePolicy1 = new InsurancePolicy();
		insurancePolicy1.setId(1L);
		insurancePolicy1.setClient(client);
		insurancePolicy1.setStatus(activeStatus);

		InsurancePolicy insurancePolicy2 = new InsurancePolicy();
		insurancePolicy2.setId(2L);
		insurancePolicy2.setClient(client);
		insurancePolicy2.setStatus(activeStatus);

		List<InsurancePolicy> insurancePolicies = List.of(insurancePolicy1, insurancePolicy2);

		when(insurancePolicyRepository.findByCustomerAndStatus(client, activeStatus)).thenReturn(insurancePolicies);

		InsurancePolicyDTO insurancePolicyDTO1 = new InsurancePolicyDTO(insurancePolicy1.getId(), client.getId(),
				insurancePolicy1.getPolicyInsuranceNumber(), insurancePolicy1.getStatus());
		InsurancePolicyDTO insurancePolicyDTO2 = new InsurancePolicyDTO(insurancePolicy2.getId(), client.getId(),
				insurancePolicy2.getPolicyInsuranceNumber(), insurancePolicy2.getStatus());

		when(insurancePolicyMapper.toDTO(insurancePolicy1)).thenReturn(insurancePolicyDTO1);
		when(insurancePolicyMapper.toDTO(insurancePolicy2)).thenReturn(insurancePolicyDTO2);

		List<InsurancePolicyDTO> result = insurancePolicyService.findPoliciesByClientAndStatus(client, activeStatus);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(insurancePolicyRepository).findByCustomerAndStatus(client, activeStatus);
		verify(insurancePolicyMapper, times(2)).toDTO(any(InsurancePolicy.class));
	}

	@Test
	void testFindPoliciesByClientAndStatusWithInvalidClient() {
	}

	@Test
	void testFindPoliciesByClientAndStatusWithNonExistentStatus() {
	}

	@Test
	void testFindInsurancePolicyByIdWithValidId() {
	}

	@Test
	void testFindInsurancePolicyByIdWithInvalidId() {
	}

	@Test
	void testFindInsurancePolicyByIdWhenPolicyDoesNotExist() {
	}

	@Test
	void testFindAllInsurancePolicyWhenPoliciesExist() {
	}

	@Test
	void testFindAllInsurancePolicyWhenNoPoliciesExist() {
	}

	@Test
	void testSavePolicyWithValidData() {
	}

	@Test
	void testSavePolicyWithInvalidData() {
	}

	@Test
	void testSavePolicyWhenRepositoryFails() {
	}

	@Test
	void testUpdateInsurancePolicyWithValidFields() {
	}

	@Test
	void testUpdateInsurancePolicyWithInvalidFields() {
	}

	@Test
	void testUpdateInsurancePolicyWhenPolicyDoesNotExist() {
	}

	@Test
	void testUpdateInsurancePolicyWithPartialData() {
	}

	@Test
	void testDeletePolicyByIdWithValidId() {
	}

	@Test
	void testDeletePolicyByIdWhenPolicyDoesNotExist() {
	}
}
