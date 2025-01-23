package api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.dto.PaymentDTO;
import application.service.PaymentService;
import domain.entity.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/Payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/payments")
	public ResponseEntity<PaymentDTO> savePayment(@RequestBody PaymentDTO paymentDTO) {
		try {
			Payment savedPayment = paymentService.savePayment(paymentDTO);
			PaymentDTO savedPaymentDTO = paymentService.getPaymentById(savedPayment.getId());
			return new ResponseEntity<>(savedPaymentDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/payments/{id}")
	public ResponseEntity<PaymentDTO> findPaymentById(@PathVariable Long id) {
		try {
			PaymentDTO paymentDTO = paymentService.getPaymentById(id);
			return ResponseEntity.ok(paymentDTO);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/payments/{id}")
	public ResponseEntity<Void> deletePaymentById(@PathVariable Long id) {
		try {
			paymentService.deletePaymentById(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
