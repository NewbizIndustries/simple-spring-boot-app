package de.is24.cloud.services;

import org.springframework.stereotype.Service;


@Service
public class ConfigService {
	private static final String DEFAULT_TIME_API_URL = "http://localhost:8080/time";

	private String timeApiUrl = DEFAULT_TIME_API_URL;

	public String getTimeApiUrl() {
		return timeApiUrl;
	}

	public void setTimeApiUrl(final String timeApiUrl) {
		this.timeApiUrl = timeApiUrl;
	}
}
