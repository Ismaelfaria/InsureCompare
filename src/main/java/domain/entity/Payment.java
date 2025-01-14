package domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Payment {

	@Id
    private Long id;
    
    private Double amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    
    @ManyToOne
    @JoinColumn(name = "insurance_policy_id")
    private InsurancePolicy insurancePolicy;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    
	public Payment(Long id, Double amount, LocalDate paymentDate, String paymentMethod, InsurancePolicy insurancePolicy,
			Client client) {
		super();
		this.id = id;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.paymentMethod = paymentMethod;
		this.insurancePolicy = insurancePolicy;
		this.client = client;
	}

	public Payment() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public InsurancePolicy getInsurancePolicy() {
		return insurancePolicy;
	}

	public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
		this.insurancePolicy = insurancePolicy;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	} 
}
