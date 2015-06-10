package de.is24.cloud.resources;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.is24.cloud.messaging.SqsQueue;

import static com.google.common.collect.Lists.newArrayList;

import static org.springframework.http.HttpStatus.OK;


@RestController
@Slf4j
public class InstanceResource {
	@Autowired
	SqsQueue instanceQueue;

	@RequestMapping("/instances")
	public ResponseEntity<String> instances() {
		log.info("resource /instances requested");

		final List<String> messages = newArrayList();
		instanceQueue.processMany(message -> {
			messages.add(message);
			return Boolean.FALSE;
		});
		return new ResponseEntity<>(StringUtils.join(messages, ", "), OK);
	}

	@RequestMapping("/instanceCount")
	public ResponseEntity<String> instanceCount() {
		log.info("resource /instanceCount requested");

		final int count = instanceQueue.count();
		return new ResponseEntity<>(count + " instances found", OK);
	}
}
