#!/usr/bin/env ruby

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

require 'aws-sdk-core'

# Before connecting to MTurk, set up your AWS account and IAM settings as described here:
# https://blog.mturk.com/how-to-use-iam-to-control-api-access-to-your-mturk-account-76fe2c2e66e2
#
# Follow AWS best practices for setting up credentials here:
# http://boto3.readthedocs.io/en/latest/guide/configuration.html

# NOTE: Example config module contains secret keys
require './mturk_config'
include MTurkConfig

# Set up AWS credentials.
credentials = Aws::Credentials.new(
  MTurkConfig.account_key,
  MTurkConfig.secret_key
)

# Use the following endpoints:
# mturk: create tasks in the live marketplace 
# sandbox: use the sandbox to publish test Human Intelligence Tasks (HITs) without paying any money. 
# Sign up for a Sandbox account at https://requestersandbox.mturk.com/ with the same credentials as your main MTurk account.
endpoints = {
  mturk: 'https://mturk-requester.us-east-1.amazonaws.com',
  sandbox: 'https://mturk-requester-sandbox.us-east-1.amazonaws.com/'
}

# Instantiate a new client for the Amazon Mechanical Turk.
mturk = Aws::MTurk::Client.new(
  endpoint: endpoints[:sandbox],
  region: 'us-east-1',
  credentials: credentials
)

# Let's check your account balance
# NOTE: In Sandbox, you should always get back $10,000
puts balance = mturk.get_account_balance.available_balance
puts "You have $#{balance}!"
puts

# Now let's create a hit!

# Step 1) Get your favorite HTML question
my_html_question = p %{
  <HTMLQuestion xmlns="http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/2011-11-11/HTMLQuestion.xsd">
    <HTMLContent><![CDATA[<!DOCTYPE html>
      <html>
        <head>
          <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
          <script type='text/javascript' src='https://s3.amazonaws.com/mturk-public/externalHIT_v1.js'></script>
        </head>
        <body>
          <form name='mturk_form' method='post' id='mturk_form' action='https://www.mturk.com/mturk/externalSubmit'>
            <input type='hidden' value='' name='assignmentId' id='assignmentId'/>
            <h1>Tell me how you really feel</h1>
            <p><textarea name='comment' cols='80' rows='3'></textarea></p>
            <p><input type='submit' id='submitButton' value='Submit' /></p>
          </form>
          <script language='Javascript'>turkSetAssignmentID();</script>
        </body>
      </html>]]>
    </HTMLContent>
    <FrameHeight>450</FrameHeight>
  </HTMLQuestion>
}.gsub(/\s+/, " ").strip

# Step 2) Get your favorite qualifications!
my_qualifications = [
  # Let's exclude workers who live in California
  Aws::MTurk::Types::QualificationRequirement.new(
    qualification_type_id: '00000000000000000071',
    comparator: 'NotEqualTo',
    locale_values: [ Aws::MTurk::Types::Locale.new( country: 'US', subdivision: 'CA' ) ]
  )
]

# Step 3) Create your hit!
puts result = mturk.create_hit(
  lifetime_in_seconds: 60 * 60 * 4,
  assignment_duration_in_seconds: 120,
  max_assignments: 1,
  reward: '0.25',
  title: 'Your Thoughts',
  description: 'Tell me what you think',
  question: my_html_question,
  qualification_requirements: my_qualifications
)

puts
puts "Once your HIT has an assignment ready to review, run the following script to approve/delte HIT:"
puts "'ruby mturk_sample_part_two_approve_and_delete_hit.rb #{result.hit.hit_id}'"
