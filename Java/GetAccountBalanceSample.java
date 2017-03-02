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
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

import com.amazonaws.services.mturk.AmazonMTurk;
import com.amazonaws.services.mturk.AmazonMTurkClientBuilder;

import com.amazonaws.services.mturk.model.GetAccountBalanceRequest;
import com.amazonaws.services.mturk.model.GetAccountBalanceResult;

/* 
 * Before connecting to MTurk, set up your AWS account and IAM settings as described here:
 * https://blog.mturk.com/how-to-use-iam-to-control-api-access-to-your-mturk-account-76fe2c2e66e2
 * 
 * Configure your AWS credentials as described here:
 * http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
 *
 */

public class GetAccountBalanceSample {

	private static final String SANDBOX_ENDPOINT = "mturk-requester-sandbox.us-east-1.amazonaws.com";
	private static final String PRODUCTION_ENDPOINT = "mturk-requester.us-east-1.amazonaws.com";
	private static final String SIGNING_REGION = "us-east-1";

	public static void main(final String[] argv) throws IOException {
		/* 
		Use the Amazon Mechanical Turk Sandbox to publish test Human Intelligence Tasks (HITs) without paying any money.
		Sign up for a Sandbox account at https://requestersandbox.mturk.com/ with the same credentials as your main MTurk account.
		*/
		final GetAccountBalanceSample sandboxApp = new GetAccountBalanceSample(getSandboxClient());
		final String sandboxBalance = sandboxApp.getAccountBalance();
		
		// In Sandbox this will always return $10,000
		System.out.println("SANDBOX - Your account balance is " + sandboxBalance);

		// Connect to the live marketplace and get your real account balance
		final GetAccountBalanceSample productionApp = new GetAccountBalanceSample(getProductionClient());
		final String productionBalance = productionApp.getAccountBalance();
		System.out.println("PRODUCTION - Your account balance is " + productionBalance);
	}

	private final AmazonMechanicalTurk client;

	private GetAccountBalanceSample(final AmazonMechanicalTurk client) {
		this.client = client;
	}

	private static AmazonMechanicalTurk getProductionClient() {
		AmazonMechanicalTurkClientBuilder builder = AmazonMechanicalTurkClientBuilder.standard();
		builder.setEndpointConfiguration(new EndpointConfiguration(PRODUCTION_ENDPOINT, SIGNING_REGION));
		return builder.build();
	}

	private static AmazonMechanicalTurk getSandboxClient() {
		AmazonMechanicalTurkClientBuilder builder = AmazonMechanicalTurkClientBuilder.standard();
		builder.setEndpointConfiguration(new EndpointConfiguration(SANDBOX_ENDPOINT, SIGNING_REGION));
		return builder.build();
	}

	private String getAccountBalance() {
		GetAccountBalanceRequest getBalanceRequest = new GetAccountBalanceRequest();
		GetAccountBalanceResult result = client.getAccountBalance(getBalanceRequest);
		return result.getAvailableBalance();
	}
}