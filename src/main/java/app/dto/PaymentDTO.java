package app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;

public record PaymentDTO( 
		@NotNull(message = "Customer ID cannot be null")
	    @Positive(message = "Customer ID must be a positive number")
	    Long customerId, 

	    @NotNull(message = "Policy ID cannot be null")
	    @Positive(message = "Policy ID must be a positive number") 
	    Long policyId, 

	    @NotNull(message = "Amount paid cannot be null")
	    @Positive(message = "Amount paid must be positive")  
	    @Digits(integer = 10, fraction = 2, message = "Amount paid must have up to 2 decimal places")  
	    Double amountPaid, 

	    @NotNull(message = "Payment method cannot be null")
	    @Size(min = 1, message = "Payment method cannot be empty")
		@Pattern(regexp = "^(Credit Card|Cash|Debit)$", message = "Invalid payment method. Allowed methods: Credit Card, Cash, Debit")
	    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Payment method must contain only letters and spaces") 
	    String paymentMethod) {}
