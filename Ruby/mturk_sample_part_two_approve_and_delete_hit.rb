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
# http://docs.aws.amazon.com/sdk-for-ruby/v1/developer-guide/set-up-creds.html

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
@mturk = Aws::MTurk::Client.new(
  endpoint: endpoints[:sandbox],
  region: 'us-east-1',
  credentials: credentials
)

# This example uses the Sandbox, so no money is involved.
def approve_all_submitted_assignments(hit_id)
  puts "Approving submitted assignments for hit id: #{hit_id}"
  # NOTE: If you have more than 100 results, you'll need to page through using
  # a unique pagination token
  @mturk.list_assignments_for_hit(hit_id: hit_id).assignments.each do |assignment|
    puts "Assignment answer submitted:"
    puts "#{assignment.answer}"
    puts
    puts "Approving assignment ... "
    @mturk.approve_assignment(
      assignment_id: assignment.assignment_id,
      requester_feedback: 'Thanks for the great work!'
    )
    puts "Approved!"
    puts
  end
end

def delete_my_hit(hit_id)
  puts "Deleting hit with id: #{hit_id}"
  @mturk.delete_hit(hit_id: hit_id)
rescue Aws::MTurk::Errors::RequestError => e
  puts "DID NOT DELETE: This hit still has unsubmitted assigments."
end

# Run script and pass in a HITId as a command line argument - see mturk_sample_part_one_create_hit.rb for generating a HIT
hit_id = ARGV[0]
while hit_id.nil? || hit_id.length.zero?
  puts "Please enter a hit id: "
  hit_id = gets.chomp!
end

approve_all_submitted_assignments(hit_id)
delete_my_hit(hit_id)
