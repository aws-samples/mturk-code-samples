# Amazon Mechanical Turk Requester API Code Samples
In 2017 [Amazon Mechanical Turk](https://www.mturk.com) (MTurk) launched support for [AWS Software Development Kits](https://aws.amazon.com/tools/). Requesters can now programmatically access MTurk with nine new SDKs.

As part of this launch, MTurk also released a [new version](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html) of the Requester API (version: ‘2017–01–17’). This version significantly updates naming conventions used in the API and adopts the latest AWS authentication and authorization standard of [Signature Version 4](http://docs.aws.amazon.com/general/latest/gr/signature-version-4.html). The API uses REST and no longer requires developers to also be familiar with SOAP. These changes make the MTurk API consistent with other AWS APIs, simplifying the on-boarding process for both new and existing AWS developers. You can explore the full API reference documentation [here](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html).

This repo contains code samples to help you get started with the AWS SDKs and the updated API. 

## Get Started

1. Set up your AWS account and your MTurk Requester and [Developer Sandbox](https://requestersandbox.mturk.com/) accounts as described [here](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMechanicalTurkGettingStartedGuide/SetUp.html).

2. Download and set up the SDK of your choice: [Python/Boto3](https://aws.amazon.com/sdk-for-python/), Javascript ([NodeJS](https://aws.amazon.com/sdk-for-node-js/) or [Browser](https://aws.amazon.com/sdk-for-browser/)), [Java](https://aws.amazon.com/sdk-for-java/), [.NET](https://aws.amazon.com/sdk-for-net/), [Go](https://aws.amazon.com/sdk-for-go/), [Ruby](https://aws.amazon.com/sdk-for-ruby/), [PHP](https://aws.amazon.com/sdk-for-php/) or [C++](https://aws.amazon.com/sdk-for-cpp/).

3. Configure the AWS SDK to use the ‘us-east-1’ region. This is the region in which the MTurk API is available.

4. Connect to the MTurk Developer Sandbox and check your account balance (the Sandbox should always return a balance of $10,000). To connect to the MTurk Developer Sandbox, set the API endpoint in your SDK to https://mturk-requester-sandbox.us-east-1.amazonaws.com. You can find examples [here](https://requester.mturk.com/developer) in various languages.

5. Explore the code samples available in this repo to see how to use the updated API and the SDKs to submit tasks and get back results from MTurk.

6. Use the [MTurk API reference](http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/Welcome.html) to explore all the operations available.
