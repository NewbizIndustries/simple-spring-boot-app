#!/bin/bash

mvn package docker:build

if [ `docker ps | wc -l` -gt 1 ]; then
	docker stop `docker ps | tail -1 | awk '{print $12}'`
fi

docker run -p 8080:8080 -t newbiz/spring-boot-docker