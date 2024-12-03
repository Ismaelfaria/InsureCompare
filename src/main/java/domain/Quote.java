package domain;

import jakarta.persistence.*;

@Entity
public class Quote {

	 	@Id
	    private Long id;  // ID of the quote
	    @ManyToOne
	    private Client client;  // Customer for whom the quote was generated
	    @ManyToOne
	    private Insurance insurance;  // Type of insurance for the quote
	    private Double quotedPrice;  // Price of the generated quote
	    
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
