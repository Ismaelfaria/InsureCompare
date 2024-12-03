package dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record PaymentDTO(
		Long id, 

	    @NotNull(message = "Customer ID cannot be null")
	    Long customerId, 

	    @NotNull(message = "Policy ID cannot be null")
	    Long policyId, 

	    @Positive(message = "Amount paid must be positive")
	    Double amountPaid, 

	    @NotNull(message = "Payment method cannot be null")
	    @Size(min = 1, message = "Payment method cannot be empty")
	    String paymentMethod) {}
