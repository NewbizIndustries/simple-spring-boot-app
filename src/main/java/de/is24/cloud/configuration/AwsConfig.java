package de.is24.cloud.configuration;

import com.amazonaws.ClientConfiguration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

import com.amazonaws.internal.StaticCredentialsProvider;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import static de.is24.cloud.configuration.AppEnvironment.isDevProfile;


@Configuration
@Slf4j
public class AwsConfig {
	@Autowired
	Environment environment;

	@Bean
	public AWSCredentialsProvider awsCredentialsProvider() {
		if (isDevProfile(environment)) {
			log.info("configure aws credentials provider: StaticCredentialsProvider");
			return new StaticCredentialsProvider(new BasicAWSCredentials(EMPTY, EMPTY));
		} else {
			log.info("configure aws credentials provider: DefaultAWSCredentialsProviderChain");
			return new DefaultAWSCredentialsProviderChain();
		}
	}

	@Bean
	public ClientConfiguration clientConfiguration() {
		return new ClientConfiguration();
	}
}
