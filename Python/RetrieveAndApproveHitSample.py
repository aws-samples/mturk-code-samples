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

import sys
import boto3
from xml.dom.minidom import parseString

# Before connecting to MTurk, set up your AWS account and IAM settings as described here:
# https://blog.mturk.com/how-to-use-iam-to-control-api-access-to-your-mturk-account-76fe2c2e66e2
#
# Follow AWS best practices for setting up credentials here:
# http://boto3.readthedocs.io/en/latest/guide/configuration.html

# Use the Amazon Mechanical Turk Sandbox to publish test Human Intelligence Tasks (HITs) without paying any money.
# Sign up for a Sandbox account at https://requestersandbox.mturk.com/ with the same credentials as your main MTurk account.

# This HIT id should be the HIT you just created - see the CreateHITSample.py file for generating a HIT
if(len(sys.argv) < 2):
    print("You must pass a HIT id as the first argument")
    sys.exit(-1)

hit_id = sys.argv[1]

# By default, we use the free-to-use Sandbox
create_hits_in_live = False

environments = {
        "live": {
            "endpoint": "https://mturk-requester.us-east-1.amazonaws.com",
            "preview": "https://www.mturk.com/mturk/preview",
            "manage": "https://requester.mturk.com/mturk/manageHITs",
            "reward": "0.00"
        },
        "sandbox": {
            "endpoint": "https://mturk-requester-sandbox.us-east-1.amazonaws.com",
            "preview": "https://workersandbox.mturk.com/mturk/preview",
            "manage": "https://requestersandbox.mturk.com/mturk/manageHITs",
            "reward": "0.11"
        },
}
mturk_environment = environments["live"] if create_hits_in_live else environments["sandbox"]

# use profile if one was passed as an arg, otherwise
profile_name = sys.argv[2] if len(sys.argv) >= 3 else None
session = boto3.Session(profile_name=profile_name)
client = session.client(
    service_name='mturk',
    region_name='us-east-1',
    endpoint_url=mturk_environment['endpoint'],
)

hit = client.get_hit(HITId=hit_id)
print 'Hit {} status: {}'.format(hit_id, hit['HIT']['HITStatus'])
response = client.list_assignments_for_hit(
    HITId=hit_id,
    AssignmentStatuses=['Submitted', 'Approved'],
    MaxResults=10,
)

assignments = response['Assignments']
print 'The number of submitted assignments is {}'.format(len(assignments))
for assignment in assignments:
    worker_id = assignment['WorkerId']
    assignment_id = assignment['AssignmentId']
    answer_xml = parseString(assignment['Answer'])

    # the answer is an xml document. we pull out the value of the first
    # //QuestionFormAnswers/Answer/FreeText
    answer = answer_xml.getElementsByTagName('FreeText')[0]
    # See https://stackoverflow.com/questions/317413
    only_answer = " ".join(t.nodeValue for t in answer.childNodes if t.nodeType == t.TEXT_NODE)

    print 'The Worker with ID {} submitted assignment {} and gave the answer "{}"'.format(worker_id, assignment_id, only_answer)

    # Approve the Assignment (if it hasn't already been approved)
    if assignment['AssignmentStatus'] == 'Submitted':
        print 'Approving Assignment {}'.format(assignment_id)
        client.approve_assignment(
            AssignmentId=assignment_id,
            RequesterFeedback='good',
            OverrideRejection=False,
        )
