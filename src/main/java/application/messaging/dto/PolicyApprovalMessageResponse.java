package application.messaging.dto;

public class PolicyApprovalMessageResponse {

	private long policyId;
	private String policyHolderName;
	private String policyStatus;
	private boolean approved;

	public PolicyApprovalMessageResponse(long policyId, String policyHolderName, String policyStatus,
			boolean approved) {
		this.policyId = policyId;
		this.policyHolderName = policyHolderName;
		this.policyStatus = policyStatus;
		this.approved = approved;
	}

	public long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(long policyId) {
		this.policyId = policyId;
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
