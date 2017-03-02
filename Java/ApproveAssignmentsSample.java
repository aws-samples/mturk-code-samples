/*
 * Copyright 2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.mturk.AmazonMTurk;
import com.amazonaws.services.mturk.AmazonMTurkClientBuilder;
import com.amazonaws.services.mturk.model.ApproveAssignmentRequest;
import com.amazonaws.services.mturk.model.Assignment;
import com.amazonaws.services.mturk.model.AssignmentStatus;
import com.amazonaws.services.mturk.model.GetHITRequest;
import com.amazonaws.services.mturk.model.GetHITResult;
import com.amazonaws.services.mturk.model.ListAssignmentsForHITRequest;
import com.amazonaws.services.mturk.model.ListAssignmentsForHITResult;

/* 
 * Before connecting to MTurk, set up your AWS account and IAM settings as described here:
 * https://blog.mturk.com/how-to-use-iam-to-control-api-access-to-your-mturk-account-76fe2c2e66e2
 * 
 * Configure your AWS credentials as described here:
 * http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
 *
 */

public class ApproveAssignmentsSample {

	// TODO Change this to your HIT ID - see CreateHITSample.java for generating a HIT
	private static final String HIT_ID_TO_APPROVE = "YOUR_HIT_ID";

	private static final String SANDBOX_ENDPOINT = "mturk-requester-sandbox.us-east-1.amazonaws.com";
	private static final String SIGNING_REGION = "us-east-1";

	public static void main(final String[] argv) throws IOException {
		final ApproveAssignmentsSample sandboxApp = new ApproveAssignmentsSample(getSandboxClient());
		sandboxApp.approveAssignment(HIT_ID_TO_APPROVE);
	}

	private final AmazonMechanicalTurk client;

	private ApproveAssignmentsSample(final AmazonMechanicalTurk client) {
		this.client = client;
	}

	/* 
	Use the Amazon Mechanical Turk Sandbox to publish test Human Intelligence Tasks (HITs) without paying any money.
	Make sure to sign up for a Sanbox account at https://requestersandbox.mturk.com/ with the same credentials as your main MTurk account.
	*/
	private static AmazonMechanicalTurk getSandboxClient() {
		AmazonMechanicalTurkClientBuilder builder = AmazonMechanicalTurkClientBuilder.standard();
		builder.setEndpointConfiguration(new EndpointConfiguration(SANDBOX_ENDPOINT, SIGNING_REGION));
		return builder.build();
	}

	private void approveAssignment(final String hitId) {

		GetHITRequest getHITRequest = new GetHITRequest();
		getHITRequest.setHITId(hitId);
		GetHITResult getHITResult = client.getHIT(getHITRequest);
		System.out.println("HIT " + hitId + " status: " + getHITResult.getHIT().getHITStatus());

		// Get a maximum of 10 completed assignments for this HIT
		ListAssignmentsForHITRequest listHITRequest = new ListAssignmentsForHITRequest();
		listHITRequest.setHITId(hitId);
		listHITRequest.setAssignmentStatuses(Collections.singletonList(AssignmentStatus.Submitted.name()));
		listHITRequest.setMaxResults(10);
		ListAssignmentsForHITResult listHITResult = client.listAssignmentsForHIT(listHITRequest);
		List<Assignment> assignmentList = listHITResult.getAssignments();
		System.out.println("The number of submitted assignments is " + assignmentList.size());

		// Iterate through all the assignments received
		for (Assignment asn : assignmentList) {
			System.out.println("The worker with ID " + asn.getWorkerId() + " submitted assignment "
					+ asn.getAssignmentId() + " and gave the answer " + asn.getAnswer());

			// Approve the assignment
			ApproveAssignmentRequest approveRequest = new ApproveAssignmentRequest();
			
			approveRequest.setAssignmentId(asn.getAssignmentId());
			approveRequest.setRequesterFeedback("Good work, thank you!");
			client.approveAssignment(approveRequest);
			
			System.out.println("The Assignment has been approved: " + asn.getAssignmentId());
		}
	}
}
