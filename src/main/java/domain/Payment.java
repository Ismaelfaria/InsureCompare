package domain;

import jakarta.persistence.*;

@Entity
public class Payment {

	  	@Id
	    private Long id;
	    @ManyToOne
	    private Client client;
	    private Double amountPaid;  
	    private String paymentMethod;  
	    
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

	    public Double getAmountPaid() {
	        return amountPaid;
	    }

	    public void setAmountPaid(Double amountPaid) {
	        this.amountPaid = amountPaid;
	    }

	    public String getPaymentMethod() {
	        return paymentMethod;
	    }

	    public void setPaymentMethod(String paymentMethod) {
	        this.paymentMethod = paymentMethod;
	    }
}
