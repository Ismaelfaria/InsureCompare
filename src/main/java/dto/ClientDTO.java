package dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public record ClientDTO(
		Long id, 

	    @NotNull(message = "Name cannot be null")
	    @Size(min = 1, message = "Name cannot be empty")
	    String name, 

	    @NotNull(message = "Email cannot be null")
	    @Email(message = "Email should be valid")
	    String email, 

	    String phone, 
	    String address) {}
