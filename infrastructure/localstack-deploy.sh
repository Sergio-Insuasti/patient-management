#!/usr/bin/env bash

# Stops the script if any command fails
set -e

# Command to deploy AWS!
aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localstack.template.json"

# Returns the load balancer and its address for future API calls
aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text
