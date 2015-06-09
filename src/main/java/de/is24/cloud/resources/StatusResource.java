package de.is24.cloud.resources;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;


@RestController
public class StatusResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(StatusResource.class);

	@Autowired
	private ConfigurableEnvironment environment;

	@RequestMapping("/env")
	public ResponseEntity<String> environment() {
		return new ResponseEntity<>(getAllProperties(environment).toString(), OK);
	}

	@RequestMapping("/health")
	public ResponseEntity<String> health() {
		LOGGER.info("resource /health requested");
		return new ResponseEntity<>("I'm fine.", OK);
	}

	@RequestMapping("/hostinfo")
	public ResponseEntity<String> hostinfo() {
		LOGGER.info("resource /hostinfo requested");

		final StringBuilder sb = new StringBuilder();
		try {
			sb.append(String.format("hostName: %s\n", InetAddress.getLocalHost().getHostName()));
			sb.append(String.format("canonicalHostName: %s\n", InetAddress.getLocalHost().getCanonicalHostName()));
			sb.append(String.format("hostAddress: %s\n", InetAddress.getLocalHost().getHostAddress()));
		} catch (UnknownHostException e) {
			return new ResponseEntity<>("UnknownHostException occurred", INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(sb.toString(), OK);
	}

	private static Map<String, Object> getAllProperties(final ConfigurableEnvironment aEnv) {
		final Map<String, Object> result = new HashMap<>();
		aEnv.getPropertySources().forEach(ps -> addAll(result, getAllProperties(ps)));
		return result;
	}

	private static Map<String, Object> getAllProperties(final PropertySource<?> aPropSource) {
		final Map<String, Object> result = new HashMap<>();

		if (aPropSource instanceof CompositePropertySource) {
			final CompositePropertySource cps = (CompositePropertySource) aPropSource;
			cps.getPropertySources().forEach(ps -> addAll(result, getAllProperties(ps)));
			return result;
		}

		if (aPropSource instanceof EnumerablePropertySource<?>) {
			final EnumerablePropertySource<?> ps = (EnumerablePropertySource<?>) aPropSource;
			Arrays.asList(ps.getPropertyNames()).forEach(key -> result.put(key, ps.getProperty(key)));
			return result;
		}

		return result;
	}

	private static void addAll(final Map<String, Object> aBase, final Map<String, Object> aToBeAdded) {
		for (final Map.Entry<String, Object> entry : aToBeAdded.entrySet()) {
			if (aBase.containsKey(entry.getKey())) {
				continue;
			}
			aBase.put(entry.getKey(), entry.getValue());
		}
	}
}
