#!/bin/bash

IMAGE=newbiz-spring-boot-docker

mvn package docker:build

docker inspect $IMAGE > /dev/null 2>&1
if [ $? == 0 ]; then
	docker rm -f $IMAGE
fi

docker run --name $IMAGE -p 8080:8080 -t newbizindustries/simple-spring-boot-app