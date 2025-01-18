package app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import application.dto.InsuranceDTO;
import application.mappers.InsuranceMapper;
import application.service.InsuranceService;
import domain.entity.Insurance;
import infra.repository.InsuranceRepository;
import jakarta.persistence.EntityNotFoundException;

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
class InsuranceServiceTest {
	@Mock
	private InsuranceRepository insuranceRepository;

	@Mock
	private InsuranceMapper insuranceMapper;

	@InjectMocks
	private InsuranceService insuranceService;

	static Insurance insurance;
	static InsuranceDTO insuranceDTO;
	static Long invalidId;

	@BeforeAll
	public static void setUpEntities() {
		insurance = new Insurance(1L, "Health Insurance", 500.0);
		insuranceDTO = new InsuranceDTO(insurance.getType(), insurance.getBasePrice());
		invalidId = 999L;
	}

	@Test
	void testGetAllInsurancesOrderedByPrice_ReturnsSortedInsuranceList() {
		List<Insurance> sortedInsurancesList = List.of(insurance);

		when(insuranceRepository.findAllSortedByPrecoBaseAsc()).thenReturn(sortedInsurancesList);
		when(insuranceMapper.toDTO(insurance))
				.thenReturn(new InsuranceDTO(insurance.getType(), insurance.getBasePrice()));

		List<InsuranceDTO> result = insuranceService.getAllInsurancesOrderedByPrice();

		assertEquals(1, result.size());
		assertEquals(insurance.getBasePrice(), result.get(0).basePrice());
	}

	@Test
	void testGetAllInsurancesOrderedByPrice_EmptyListReturnsEmpty() {
		when(insuranceRepository.findAllSortedByPrecoBaseAsc()).thenReturn(List.of());

		List<InsuranceDTO> result = insuranceService.getAllInsurancesOrderedByPrice();

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void testGetInsuranceById_ValidIdReturnsInsurance() {
		when(insuranceRepository.findById(insurance.getId())).thenReturn(Optional.of(insurance));
		when(insuranceMapper.toDTO(insurance)).thenReturn(insuranceDTO);

		InsuranceDTO result = insuranceService.getInsuranceById(insurance.getId());

		assertNotNull(result);
		assertEquals(insurance.getType(), result.type());
		assertEquals(insurance.getBasePrice(), result.basePrice());
	}

	@Test
	void testGetInsuranceById_InvalidIdThrowsEntityNotFoundException() {
		Long invalidId = 999L;

		when(insuranceRepository.findById(invalidId)).thenReturn(Optional.empty());

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			insuranceService.getInsuranceById(invalidId);
		});
		assertEquals("Insurance not found with id: " + invalidId, thrown.getMessage());
	}

	@Test
	void testSaveInsurance_ValidDTO_SavesAndReturnsInsurance() {
		when(insuranceRepository.save(insurance)).thenReturn(insurance);
		when(insuranceMapper.toEntity(insuranceDTO)).thenReturn(insurance);

		Insurance result = insuranceService.saveInsurance(insuranceDTO);

		assertNotNull(result);
		assertEquals(insurance.getType(), result.getType());
		assertEquals(insurance.getBasePrice(), result.getBasePrice());
	}

	@Test
	void testSaveInsurance_InvalidDTO_ThrowsException() {
		InsuranceDTO invalidInsuranceDTO = new InsuranceDTO(null, null);
		when(insuranceRepository.save(null)).thenThrow(new IllegalArgumentException("Invalid Insurance data"));

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			insuranceService.saveInsurance(invalidInsuranceDTO);
		});
		assertEquals("Invalid Insurance data", thrown.getMessage());
	}

	@Test
	void testUpdateInsurance_ValidIdAndUpdateRequest_UpdatesAndReturnsInsurance() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("type", "Updated Health Insurance");
		updateRequest.put("baseprice", 600.0);

		when(insuranceRepository.findById(insurance.getId())).thenReturn(Optional.of(insurance));
		when(insuranceRepository.save(insurance)).thenReturn(insurance);
		when(insuranceMapper.toDTO(insurance)).thenReturn(insuranceDTO);

		InsuranceDTO result = insuranceService.updateInsurance(insurance.getId(), updateRequest);

		assertNotNull(result);
		assertEquals(insuranceDTO.type(), result.type());
		assertEquals(insuranceDTO.basePrice(), result.basePrice());
	}

	@Test
	void testUpdateInsurance_InvalidId_ThrowsEntityNotFoundException() {
		Map<String, Object> updateRequest = new HashMap<>();

		when(insuranceRepository.findById(invalidId)).thenReturn(Optional.empty());

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			insuranceService.updateInsurance(invalidId, updateRequest);
		});
		assertEquals("Insurance not found with id: " + invalidId, thrown.getMessage());
	}

	@Test
	void testUpdateInsurance_InvalidField_ThrowsIllegalArgumentException() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("invalidField", "someValue");

		when(insuranceRepository.findById(insurance.getId())).thenReturn(Optional.of(insurance));

		IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
			insuranceService.updateInsurance(insurance.getId(), updateRequest);
		});

		assertEquals("Invalid field to update: invalidField", thrown.getMessage());
	}

	@Test
	void testDeleteInsuranceById_ValidId_DeletesInsurance() {
		when(insuranceRepository.existsById(insurance.getId())).thenReturn(true);
		doNothing().when(insuranceRepository).deleteById(insurance.getId());

		insuranceService.deleteInsuranceById(insurance.getId());

		verify(insuranceRepository, times(1)).deleteById(insurance.getId());
	}

	@Test
	void testDeleteInsuranceById_InvalidId_ThrowsEntityNotFoundException() {
		when(insuranceRepository.existsById(invalidId)).thenReturn(false);

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			insuranceService.deleteInsuranceById(invalidId);
		});

		assertEquals("Insurance not found with id: " + invalidId, thrown.getMessage());
	}
}
