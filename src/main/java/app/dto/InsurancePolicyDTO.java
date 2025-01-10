package app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record InsurancePolicyDTO( 
	    @NotNull(message = "Customer ID cannot be null")
	    Long customerId, 

	    @NotNull(message = "Insurance ID cannot be null")
	    Long insuranceId, 

	    @NotNull(message = "Policy number cannot be null")
	    @Size(min = 1, message = "Policy number cannot be empty")
	    String policyNumber, 

	    @NotNull(message = "Status cannot be null")
	    @Size(min = 1, message = "Status cannot be empty")
	    String status) {}
