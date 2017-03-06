# Copyright 2017 Amazon.com, Inc. or its affiliates

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

# http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import boto3

# Before connecting to MTurk, set up your AWS account and IAM settings as described here:
# https://blog.mturk.com/how-to-use-iam-to-control-api-access-to-your-mturk-account-76fe2c2e66e2
#
# Follow AWS best practices for setting up credentials here:
# http://boto3.readthedocs.io/en/latest/guide/configuration.html 

# Use the Amazon Mechanical Turk Sandbox to publish test Human Intelligence Tasks (HITs) without paying any money.
# Sign up for a Sandbox account at https://requestersandbox.mturk.com/ with the same credentials as your main MTurk account.

client = boto3.client(
    service_name='mturk',
    endpoint_url='https://mturk-requester-sandbox.us-east-1.amazonaws.com'
)

# Uncomment the below to connect to the live marketplace
# Region is always us-east-1
# client = boto3.client(service_name = 'mturk', region_name='us-east-1')

# This HIT id should be the HIT you just created - see the CreateHITSample.py file for generating a HIT
hit_id = 'YOUR_HIT_ID'

hit = client.get_hit(HITId=hit_id)
print 'Hit {} status: {}'.format(hit_id, hit['HIT']['HITStatus'])
response = client.list_assignments_for_hit(
    HITId=hit_id,
    AssignmentStatuses=['Submitted'], 
    MaxResults=10
)

assignments = response['Assignments']
print 'The number of submitted assignments is {}'.format(len(assignments))
for assignment in assignments:
    WorkerId = assignment['WorkerId']
    assignmentId = assignment['AssignmentId']
    answer = assignment['Answer']
    print 'The Worker with ID {} submitted assignment {} and gave the answer {}'.format(WorkerId,assignmentId, answer)

    # Approve the Assignment
    print 'Approve Assignment {}'.format(assignmentId)
    client.approve_assignment(
        AssignmentId=assignmentId,
        RequesterFeedback='good',
        OverrideRejection=False
    )
