package de.is24.cloud.configuration;

import com.google.common.collect.ImmutableSet;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import org.springframework.core.env.Environment;

import static com.google.common.base.Preconditions.checkNotNull;


public final class AppEnvironment {
	private static final String PROPERTY_STACK_NAME = "STACK_NAME";
	private static final String PROPERTY_STACK_VERSION = "STACK_VERSION";
	private static final String PROPERTY_IMAGE_VERSION = "IMAGE_VERSION";
	private static final String PROPERTY_TIMEAPI_URL = "TIMEAPI_URL";

	private static final String SQS_HOST = "sqs.local.host";
	private static final String SQS_PORT = "sqs.local.port";

	private static final String DEFAULT_SQS_HOST = "localhost";
	private static final String DEFAULT_SQS_PORT = "9324";

	private static final String DEV_PROFILE = "development";

	private AppEnvironment() {
	}

	public static boolean isDevProfile(final Environment environment) {
		return ImmutableSet.copyOf(environment.getActiveProfiles()).contains(DEV_PROFILE);
	}

	public static Optional<String> getStackName(final Environment environment) {
		checkNotNull(environment);

		final String name = environment.getProperty(PROPERTY_STACK_NAME);
		return StringUtils.isNotBlank(name) ? Optional.of(name) : Optional.empty();
	}

	public static Optional<String> getStackVersion(final Environment environment) {
		checkNotNull(environment);

		final String version = environment.getProperty(PROPERTY_STACK_VERSION);
		return StringUtils.isNotBlank(version) ? Optional.of(version) : Optional.empty();
	}

	public static Optional<String> getImageVersion(final Environment environment) {
		checkNotNull(environment);

		final String version = environment.getProperty(PROPERTY_IMAGE_VERSION);
		return StringUtils.isNotBlank(version) ? Optional.of(version) : Optional.empty();
	}

	public static Optional<String> getTimeApiUrl(final Environment environment) {
		checkNotNull(environment);

		final String url = environment.getProperty(PROPERTY_TIMEAPI_URL);
		return StringUtils.isNotBlank(url) ? Optional.of(url) : Optional.empty();
	}

	public static String sqsHost(final Environment environment) {
		checkNotNull(environment);

		if (environment.containsProperty(SQS_HOST)) {
			return environment.getProperty(SQS_HOST);
		} else {
			return DEFAULT_SQS_HOST;
		}
	}

	public static String sqsPort(final Environment environment) {
		checkNotNull(environment);

		if (environment.containsProperty(SQS_PORT)) {
			return environment.getProperty(SQS_PORT);
		} else {
			return DEFAULT_SQS_PORT;
		}
	}
}
