package domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class InsurancePolicy {
	@Id
	private Long id;
	@ManyToOne
	private Client client;
	@ManyToOne
	private Insurance insurance;
	private String policyInsuranceNumber;
	private String status;

	public InsurancePolicy(Long id, Client client, Insurance insurance, String policyInsuranceNumber, String status) {
		super();
		this.id = id;
		this.client = client;
		this.insurance = insurance;
		this.policyInsuranceNumber = policyInsuranceNumber;
		this.status = status;
	}

	public InsurancePolicy() {
		super();
	}

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

	public String getPolicyInsuranceNumber() {
		return policyInsuranceNumber;
	}

	public void setPolicyInsuranceNumber(String policyInsuranceNumber) {
		this.policyInsuranceNumber = policyInsuranceNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
