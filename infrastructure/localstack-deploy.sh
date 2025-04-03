#!/usr/bin/env bash

set -e

aws --endpoint-url=http://localhost:4566 cloudformation delete-stack \
    --stack-name user-management

aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name user-management \
    --template-file "./cdk.out/localstack.template.json"

aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text