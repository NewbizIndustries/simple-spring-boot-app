package de.is24.cloud.resources;

import com.amazonaws.regions.Regions;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.amazonaws.regions.ServiceAbbreviations.Dynamodb;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import static de.is24.cloud.configuration.AppEnvironment.getImageVersion;
import static de.is24.cloud.configuration.AppEnvironment.getStackVersion;


@RestController
@Slf4j
public class StatusResource {
	@Autowired
	private Environment environment;

	@Autowired
	private ConfigurableEnvironment configurableEnvironment;

	@RequestMapping("/env")
	public ResponseEntity<String> environment() {
		return new ResponseEntity<>(getAllProperties(configurableEnvironment).toString(), OK);
	}

	@RequestMapping("/health")
	public ResponseEntity<String> health() {
		log.info("resource /health requested");
		return new ResponseEntity<>("I'm fine.", OK);
	}

	@RequestMapping("/image")
	public ResponseEntity<String> imageVersion() {
		log.info("resource /image requested");

		final Optional<String> imageVersion = getImageVersion(environment);
		if (imageVersion.isPresent()) {
			return new ResponseEntity<>(configurableEnvironment.getProperty(imageVersion.get()), OK);
		}
		return new ResponseEntity<>("unknown image version", OK);
	}

	@RequestMapping("/stack")
	public ResponseEntity<String> stackVersion() {
		log.info("resource /stack requested");

		final Optional<String> stackVersion = getStackVersion(environment);
		if (stackVersion.isPresent()) {
			return new ResponseEntity<>(configurableEnvironment.getProperty(stackVersion.get()), OK);
		}
		return new ResponseEntity<>("unknown stack version", OK);
	}

	@RequestMapping("/host")
	public ResponseEntity<String> hostinfo() {
		log.info("resource /host requested");

		final StringBuilder sb = new StringBuilder();
		try {
			sb.append(String.format("hostName: %s\n", InetAddress.getLocalHost().getHostName()));
			sb.append(String.format("canonicalHostName: %s\n", InetAddress.getLocalHost().getCanonicalHostName()));
			sb.append(String.format("hostAddress: %s\n", InetAddress.getLocalHost().getHostAddress()));
			sb.append(String.format("serviceEndpoint: %s\n", serviceEndpoint()));
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

	private String serviceEndpoint() {
		try {
			return Regions.getCurrentRegion().getServiceEndpoint(Dynamodb);
		} catch (NullPointerException e) {
			return "no service endpoint found";
		}
	}
}
