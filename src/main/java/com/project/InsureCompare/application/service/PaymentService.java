package com.project.InsureCompare.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.PaymentDTO;
import com.project.InsureCompare.application.mappers.PaymentMapper;
import com.project.InsureCompare.domain.entity.Payment;
import com.project.InsureCompare.infra.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private PaymentMapper paymentMapper;

	public Payment savePayment(PaymentDTO paymentDTO) {
		Payment payment = paymentMapper.toEntity(paymentDTO);
		return paymentRepository.save(payment);
	}

	public PaymentDTO getPaymentById(Long id) {
		return paymentRepository.findById(id).map(paymentMapper::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + id));
	}

	public void deletePaymentById(Long id) {
		if (!paymentRepository.existsById(id)) {
			throw new EntityNotFoundException("Payment not found with id: " + id);
		}
		paymentRepository.deleteById(id);
	}
}