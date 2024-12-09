package Mappers;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import domain.Client;
import domain.InsurancePolicy;
import domain.Payment;
import dto.PaymentDTO;
import repository.ClientRepository;
import repository.InsurancePolicyRepository;

@Component
public class PaymentMapper {

	private final ClientRepository clientRepository;
	private final InsurancePolicyRepository insurancePolicyRepository;

	public PaymentMapper(ClientRepository clientRepository, InsurancePolicyRepository insurancePolicyRepository) {
		this.clientRepository = clientRepository;
		this.insurancePolicyRepository = insurancePolicyRepository;
	}

	public Payment toEntity(PaymentDTO dto) {
		Payment payment = new Payment();

		Client client = clientRepository.findById(dto.customerId())
				.orElseThrow(() -> new IllegalArgumentException("Client with ID " + dto.customerId() + " not found"));

		InsurancePolicy insurancePolicy = insurancePolicyRepository.findById(dto.policyId())
				.orElseThrow(() -> new IllegalArgumentException("Policy with ID " + dto.policyId() + " not found"));

		payment.setClient(client);
		payment.setInsurancePolicy(insurancePolicy);
		payment.setAmount(dto.amountPaid());
		payment.setPaymentMethod(dto.paymentMethod());
		payment.setPaymentDate(LocalDate.now()); 

		return payment;
	}

	public PaymentDTO toDTO(Payment payment) {
		return new PaymentDTO(payment.getClient().getId(), payment.getInsurancePolicy().getId(), payment.getAmount(),
				payment.getPaymentMethod());
	}

}
