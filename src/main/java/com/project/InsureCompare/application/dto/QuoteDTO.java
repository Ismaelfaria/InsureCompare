package com.project.InsureCompare.application.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record QuoteDTO(
	    @NotNull(message = "Customer ID cannot be null")
	    Long clientId, 

	    @NotNull(message = "Insurance ID cannot be null")
	    Long insuranceId, 

	    @Positive(message = "Quoted price must be positive")
	    Double quotedPrice) {}
