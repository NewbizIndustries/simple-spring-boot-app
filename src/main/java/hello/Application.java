package hello;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.OK;


@RestController
@SpringBootApplication
public class Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	private static final String PROPERTY_TIME_API = "time.api";
	private static final String DEFAULT_TIME_API_URL = "http://localhost:8080/time";

	@Autowired
	private Environment environment;

	private final RestTemplate restTemplate = new RestTemplate();

	@RequestMapping("/")
	public String home() {
		LOGGER.info("resource / requested");

		final ResponseEntity<CurrentDate> responseEntity;
		try {
			responseEntity = restTemplate.getForEntity(urlOfTimeApi(), CurrentDate.class);
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
		LOGGER.info("resource /time requested");
		return new ResponseEntity<>(new CurrentDate(), OK);
	}

	@RequestMapping("/health")
	public ResponseEntity<String> health() {
		LOGGER.info("resource /health requested");
		return new ResponseEntity<>("I'm fine.", OK);
	}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private String urlOfTimeApi() {
		final String timeApiUrl;
		if (environment.containsProperty(PROPERTY_TIME_API)) {
			timeApiUrl = environment.getProperty(PROPERTY_TIME_API);
		} else {
			timeApiUrl = DEFAULT_TIME_API_URL;
		}
		LOGGER.info("using time API at: " + timeApiUrl);
		return timeApiUrl;
	}

	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}
}
