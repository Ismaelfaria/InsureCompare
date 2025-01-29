package app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.InsureCompare.application.dto.PaymentDTO;
import com.project.InsureCompare.application.mappers.PaymentMapper;
import com.project.InsureCompare.application.service.PaymentService;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.domain.entity.InsurancePolicy;
import com.project.InsureCompare.domain.entity.Payment;
import com.project.InsureCompare.infra.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private PaymentMapper paymentMapper;

	@InjectMocks
	private PaymentService paymentService;

	static Payment payment;
	static PaymentDTO paymentDTO;
	static Long invalidId;

	@BeforeAll
	public static void setUpEntities() {
		Client client = new Client(1L, "John Doe", "john.doe@gmail.com", "123456789", "123 Main St");
		Insurance insurance = new Insurance(1L, "Health Insurance", 500.0);
		InsurancePolicy insurancePolicy = new InsurancePolicy(1L, client, insurance, "12345", "ACTIVE");

		payment = new Payment(1L, 500.0, LocalDate.now(), "Credit Card", insurancePolicy, client);
		paymentDTO = new PaymentDTO(payment.getClient().getId(), payment.getId(), payment.getAmount(),
				payment.getPaymentMethod());
		invalidId = 999L;
	}

	@Test
	void testSavePayment_ValidDTO_SavesAndReturnsPayment() {
		when(paymentMapper.toEntity(paymentDTO)).thenReturn(payment);
		when(paymentRepository.save(payment)).thenReturn(payment);

		Payment result = paymentService.savePayment(paymentDTO);

		assertNotNull(result);
		assertEquals(payment.getAmount(), result.getAmount());
		assertEquals(payment.getPaymentMethod(), result.getPaymentMethod());
	}

	@Test
	void testGetPaymentById_ValidIdReturnsPayment() {
		when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));
		when(paymentMapper.toDTO(payment)).thenReturn(paymentDTO);

		PaymentDTO result = paymentService.getPaymentById(payment.getId());

		assertNotNull(result);
		assertEquals(payment.getAmount(), result.amount());
		assertEquals(payment.getPaymentMethod(), result.paymentMethod());
	}

	@Test
	void testGetPaymentById_InvalidIdThrowsEntityNotFoundException() {
		when(paymentRepository.findById(invalidId)).thenReturn(Optional.empty());

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			paymentService.getPaymentById(invalidId);
		});
		assertEquals("Payment not found with id: " + invalidId, thrown.getMessage());
	}

	@Test
	void testDeletePaymentById_ValidId_DeletesPayment() {
		when(paymentRepository.existsById(payment.getId())).thenReturn(true);
		doNothing().when(paymentRepository).deleteById(payment.getId());

		paymentService.deletePaymentById(payment.getId());

		verify(paymentRepository, times(1)).deleteById(payment.getId());
	}

	@Test
	void testDeletePaymentById_InvalidIdThrowsEntityNotFoundException() {
		when(paymentRepository.existsById(invalidId)).thenReturn(false);

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			paymentService.deletePaymentById(invalidId);
		});
		assertEquals("Payment not found with id: " + invalidId, thrown.getMessage());
	}
}
