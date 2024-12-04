package dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;

public record InsuranceDTO(
	    @NotNull(message = "Insurance type cannot be null")
	    @Size(min = 1, message = "Insurance type cannot be empty")
	    String type, 

	    @Positive(message = "Base price must be positive")
	    Double basePrice) {}
