package domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Quote {

	 	@Id
	    private Long id;  
	    @ManyToOne
	    private Client client;  
	    @ManyToOne
	    private Insurance insurance;  
	    private Double quotedPrice;  
	    
	    // Getters and Setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Client getClient() {
	        return client;
	    }

	    public void setClient(Client client) {
	        this.client = client;
	    }

	    public Insurance getInsurance() {
	        return insurance;
	    }

	    public void setInsurance(Insurance insurance) {
	        this.insurance = insurance;
	    }

	    public Double getQuotedPrice() {
	        return quotedPrice;
	    }

	    public void setQuotedPrice(Double quotedPrice) {
	        this.quotedPrice = quotedPrice;
	    }
}
