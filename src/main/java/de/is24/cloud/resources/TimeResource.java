package de.is24.cloud.resources;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.is24.cloud.domain.CurrentDate;
import de.is24.cloud.services.ConfigService;
import de.is24.cloud.utils.http.HttpClientRequestFactoryBuilder;

import static org.springframework.http.HttpStatus.OK;

import static de.is24.cloud.utils.HostUtils.getHostname;


@RestController
@Slf4j
public class TimeResource {
	@Autowired
	private ConfigService configService;

	@Autowired
	private Environment environment;

	private final RestTemplate restTemplate = new RestTemplate(new HttpClientRequestFactoryBuilder().buildUnsafe());

	@RequestMapping("/")
	public String home() {
		log.info("resource / requested");

		final ResponseEntity<CurrentDate> responseEntity;
		try {
			responseEntity = restTemplate.getForEntity(configService.getTimeApiUrl(), CurrentDate.class);
		} catch (RestClientException | IllegalArgumentException e) {
			return "Fehler beim Zugriff auf Time API: " + e.getMessage();
		}
		if (responseEntity.getStatusCode() != OK) {
			return "Die Time API konnte die Anfrage nicht korrekt bearbeiten.";
		}

		final CurrentDate currentDate = responseEntity.getBody();
		final String timeMessage = String.format("Heute haben wir den %s. Aktuelle Uhrzeit: %s.<br>\n",
			currentDate.getDate(), currentDate.getTime());
		final String serverMessage = String.format("fe server: %s; time server: %s", getHostname(),
			currentDate.getOrigin());

		return timeMessage + serverMessage;
	}

	@RequestMapping(value = "/time", produces = "application/json")
	public ResponseEntity<CurrentDate> timestamp() {
		log.info("resource /time requested");
		return new ResponseEntity<>(new CurrentDate(), OK);
	}
}
