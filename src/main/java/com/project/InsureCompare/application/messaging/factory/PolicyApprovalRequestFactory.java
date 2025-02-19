package com.project.InsureCompare.application.messaging.factory;

import com.project.InsureCompare.application.messaging.dto.PolicyApprovalMessageRequest;
import com.project.InsureCompare.domain.entity.InsurancePolicy;

public class PolicyApprovalRequestFactory {

	public PolicyApprovalMessageRequest createRequest(InsurancePolicy insurancePolicy) {
		PolicyApprovalMessageRequest request = new PolicyApprovalMessageRequest();
		request.setClientId(insurancePolicy.getClient().getId());
		request.setInsuranceId(insurancePolicy.getInsurance().getId());
		request.setPolicyHolderNumber(insurancePolicy.getPolicyInsuranceNumber());
		request.setPolicyStatus(insurancePolicy.getStatus());
		
		return request;
	}
}
