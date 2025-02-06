package com.project.InsureCompare.application.messaging.dto;

public class PolicyApprovalMessageRequest {

	private long clientId;
	private long insuranceId;
	private String policyHolderNumber;
	private String policyStatus;

	public PolicyApprovalMessageRequest() {
		super();
	}

	public PolicyApprovalMessageRequest(long clientId, long insuranceId, String policyHolderNumber,
			String policyStatus) {
		super();
		this.clientId = clientId;
		this.insuranceId = insuranceId;
		this.policyHolderNumber = policyHolderNumber;
		this.policyStatus = policyStatus;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(long insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getPolicyHolderNumber() {
		return policyHolderNumber;
	}

	public void setPolicyHolderNumber(String policyHolderNumber) {
		this.policyHolderNumber = policyHolderNumber;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

}