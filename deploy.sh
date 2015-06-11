#!/bin/bash

IMAGE=simple-spring-boot-app

mvn package docker:build

docker inspect elasticmq > /dev/null 2>&1
if [ $? == 1 ]; then
    echo starting elasticmq server
    docker run -d --name elasticmq visenze/elasticmq
fi

docker inspect ${IMAGE} > /dev/null 2>&1
if [ $? == 0 ]; then
	docker rm -f ${IMAGE}
fi

docker run --name ${IMAGE} -p 8080:8080 --link elasticmq:sqs -e "sqs.local.host=sqs" -e "sqs.local.port=80" -t immobilienscout24/simple-spring-boot-app