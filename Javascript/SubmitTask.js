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

fs = require('fs');

/**
 * Use the Amazon Mechanical Turk Sandbox to publish test Human Intelligence Tasks (HITs) without paying any
 * money. Sign up for a Sandbox account at https://requestersandbox.mturk.com/ with the same credentials as
 * your main MTurk account.
 */

var endpoint = 'https://mturk-requester-sandbox.us-east-1.amazonaws.com';
// Uncomment this line to use in production
// endpoint = 'https://mturk-requester.us-east-1.amazonaws.com';

// Connect to sandbox
var mturk = new AWS.MTurk({ endpoint: endpoint });

// Test your ability to connect to MTurk by checking your account balance
mturk.getAccountBalance(function (err, data) {
    if (err) {
        console.log(err.message);
    } else {
        // Sandbox balance check will always return $10,000
        console.log('I have ' + data.AvailableBalance + ' in my account.');
    }
})

/* 
Publish a new HIT to the Sandbox marketplace start by reading in the HTML markup specifying your task from a seperate file (my_question.xml). To learn more about the HTML question type, see here: http://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_HTMLQuestionArticle.html
*/ 

fs.readFile('my_question.xml', 'utf8', function (err, myQuestion) {
    if (err) {
        return console.log(err);
    }

    // Construct the HIT object below
    var myHIT = {
        Title: 'This is a new test question',
        Description: 'Another description',
        MaxAssignments: 1,
        LifetimeInSeconds: 3600,
        AssignmentDurationInSeconds: 600,
        Reward: '0.20',
        Question: myQuestion,

        // Add a qualification requirement that the Worker must be either in Canada or the US 
        QualificationRequirements: [
            {
                QualificationTypeId: '00000000000000000071',
                Comparator: 'In',
                LocaleValues: [
                    { Country: 'US' },
                    { Country: 'CA' },
                ],
            },
        ],
    };

    // Publish the object created above
    mturk.createHIT(myHIT, function (err, data) {
        if (err) {
            console.log(err.message);
        } else {
            console.log(data);
            // Save the HITId printed by data.HIT.HITId and use it in the RetrieveAndApproveResults.js code sample
            console.log('HIT has been successfully published here: https://workersandbox.mturk.com/mturk/preview?groupId=' + data.HIT.HITTypeId + ' with this HITId: ' + data.HIT.HITId);
        }
    })
});