package service;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Payment;
import jakarta.persistence.EntityNotFoundException;
import repository.PaymentRepository;

public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
    }

    public void deletePaymentById(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }
}