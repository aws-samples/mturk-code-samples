/**
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

var util = require('util');
var AWS = require('aws-sdk');

/**
 * Before connecting to MTurk, set up your AWS account and IAM settings as described here:
 * https://blog.mturk.com/how-to-use-iam-to-control-api-access-to-your-mturk-account-76fe2c2e66e2
 *
 * Follow AWS best practices for setting credentials from here:
 * http://docs.aws.amazon.com/sdk-for-javascript/v2/developer-guide/setting-credentials-node.html
 */
AWS.config.loadFromPath('./config.json');

/**
 * Use the Amazon Mechanical Turk Sandbox to publish test Human Intelligence Tasks (HITs) without paying any
 * money. Sign up for a Sandbox account at https://requestersandbox.mturk.com/ with the same credentials as
 * your main MTurk account.
 */

// Add in the HITId below. See SubmitTask.js for generating a HIT
var myHITId = 'YOUR_HIT_ID';

var endpoint = 'https://mturk-requester-sandbox.us-east-1.amazonaws.com';
// Uncomment this line to use in production
// endpoint = 'https://mturk-requester.us-east-1.amazonaws.com';

// Connect to sandbox
var mturk = new AWS.MTurk({ endpoint: endpoint });

/**
 * To keep this example simple, we are assuming that there are fewer
 * than 100 results and there is no need to iterate through pages of results
 */

mturk.listAssignmentsForHIT({HITId: myHITId}, function (err, assignmentsForHIT) {
    if (err) {
        console.log(err.message);
    } else {
        console.log('Completed Assignments found: ' + assignmentsForHIT.NumResults);
        for (var i = 0; i < assignmentsForHIT.NumResults; i++) {
            console.log('Answer from Worker with ID - ' + assignmentsForHIT.Assignments[i].WorkerId + ': ', assignmentsForHIT.Assignments[i].Answer);

            // Approve the work so the Worker is paid with and optional feedback message             
            mturk.approveAssignment({
                AssignmentId: assignmentsForHIT.Assignments[i].AssignmentId,
                RequesterFeedback: 'Thanks for the great work!',
            }, function (err) {
                if (err) {
                    console.log(err, err.stack);
                }
            });
        }
    }
});