package application.dtoMessage;

public class PolicyApprovalMessageRequest {

	private long policyId;
	private String policyHolderNumber;
	private String policyStatus;

	public PolicyApprovalMessageRequest() {
		super();
	}

	public PolicyApprovalMessageRequest(long policyId, String policyHolderName, String policyStatus) {
		super();
		this.policyId = policyId;
		this.policyHolderNumber = policyHolderName;
		this.policyStatus = policyStatus;
	}

	public long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(long policyId) {
		this.policyId = policyId;
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
