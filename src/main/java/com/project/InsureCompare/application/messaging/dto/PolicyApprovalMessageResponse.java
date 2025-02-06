package com.project.InsureCompare.application.messaging.dto;

public class PolicyApprovalMessageResponse {

	private long clientId;
	private long insuranceId;
	private String policyHolderName;
	private String policyStatus;
	private boolean approved;

	public PolicyApprovalMessageResponse() {
		super();
	}

	public PolicyApprovalMessageResponse(long clientId, long insuranceId, String policyHolderName, String policyStatus,
			boolean approved) {
		super();
		this.clientId = clientId;
		this.insuranceId = insuranceId;
		this.policyHolderName = policyHolderName;
		this.policyStatus = policyStatus;
		this.approved = approved;
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

	public String getPolicyHolderName() {
		return policyHolderName;
	}

	public void setPolicyHolderName(String policyHolderName) {
		this.policyHolderName = policyHolderName;
	}

	public String getPolicyStatus() {
		return policyStatus;
	}

	public void setPolicyStatus(String policyStatus) {
		this.policyStatus = policyStatus;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
}