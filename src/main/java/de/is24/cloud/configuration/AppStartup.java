package de.is24.cloud.configuration;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import de.is24.cloud.messaging.SqsQueue;
import de.is24.cloud.services.ConfigService;

import static de.is24.cloud.configuration.AppEnvironment.getTimeApiUrl;


@Component
@Slf4j
public class AppStartup implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	Environment environment;

	@Autowired
	ConfigService configService;

	@Autowired
	SqsQueue instanceQueue;

	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		registerHost();
		configureTimeApiUrl();
	}

	private void registerHost() {
		try {
			final String hostName = InetAddress.getLocalHost().getHostName();
			instanceQueue.send(hostName);
			log.info("application started on {}", hostName);
		} catch (UnknownHostException e) {
			log.error("UnknownHostException occurred");
		}
	}

	private void configureTimeApiUrl() {
		final Optional<String> timeApiUrl = getTimeApiUrl(environment);
		if (timeApiUrl.isPresent()) {
			log.info("set time api url to {}", timeApiUrl.get());
			configService.setTimeApiUrl(timeApiUrl.get());
		}
	}
}
