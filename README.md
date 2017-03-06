# Amazon Mechanical Turk Requester API Code Samples
On February 27th 2017, Amazon Mechanical Turk (MTurk) launched support for the [AWS Software Development Kits](https://aws.amazon.com/tools/). Requesters can now programmatically access MTurk with eight SDKs and can use the same tools to access over 50 other AWS services.

As part of this launch, MTurk also released a [new version](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html) (‘2017–01–17’) of the Requester API. This version significantly updates naming conventions used in the API and adopts the latest and most secure AWS authentication and authorization standard of Signature Version 4 which is a significant improvement over our previous authentication mechanism. The API uses REST requests to exchange data and no longer requires developers to also be familiar with SOAP. These changes make the MTurk API more consistent with other AWS APIs, simplifying the on-boarding process for both new and existing AWS developers. You can explore the full API reference documentation here.

This repo contains code samples to help you get started with the AWS SDKs and the updated API. 

## Get Started

1. Set up your AWS account and your MTurk Requester and [Developer Sandbox](https://requestersandbox.mturk.com/) accounts as described [here](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMechanicalTurkGettingStartedGuide/SetUp.html).

2. Download and set up the SDK of your choice: [Python/Boto3](https://aws.amazon.com/sdk-for-python/), Javascript ([NodeJS](https://aws.amazon.com/sdk-for-node-js/) or [browser](https://aws.amazon.com/sdk-for-browser/)), [Java](https://aws.amazon.com/sdk-for-java/), [.NET](https://aws.amazon.com/sdk-for-net/), [Go](https://aws.amazon.com/sdk-for-go/), [Ruby](https://aws.amazon.com/sdk-for-ruby/) or [C++](https://aws.amazon.com/sdk-for-cpp/).

3. Try connecting to the API by [checking your account balance](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_GetAccountBalanceOperation.html). Connect to the Developer Sandbox and do the same check (you should get back $10,000). You can connect to the Sandbox by overriding the API endpoint in your SDK to https://mturk-requester-sandbox.us-east-1.amazonaws.com.

4. Explore the code samples available in this repo to see how to use the updated API and the SDKs to submit tasks and get back results from MTurk.

5. Use the [MTurk API reference](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html) to explore all the operations available.

*Tip: when configuring any AWS SDK, note that the MTurk API is available only in the “us-east-1” region
