package hello;

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
	private static final String PROPERTY_TIME_API = "time.api";
	private static final String DEFAULT_TIME_API_URL = "http://localhost:8080/time";

	@Autowired
	private Environment environment;

	private final RestTemplate restTemplate = new RestTemplate();

	@RequestMapping("/")
	public String home() {
		final ResponseEntity<CurrentDate> responseEntity;
		try {
			responseEntity = restTemplate.getForEntity(urlOfTimeApi(), CurrentDate.class);
		} catch (RestClientException | IllegalArgumentException e) {
			return "Fehler beim Zugriff auf Time API";
		}
		if (responseEntity.getStatusCode() != OK) {
			return "Die Time API konnte die Anfrage nicht korrekt bearbeiten.";
		}

		final CurrentDate currentDate = responseEntity.getBody();
		return String.format("Heute haben wir den %s. Aktuelle Uhrzeit: %s.", currentDate.getDate(), currentDate.getTime());
	}

	@RequestMapping(value = "/time", produces = "application/json")
	public ResponseEntity<CurrentDate> timestamp() {
		return new ResponseEntity<>(new CurrentDate(), OK);
	}

	@RequestMapping("/health")
	public ResponseEntity<String> health() {
		return new ResponseEntity<>("I'm fine.", OK);
	}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private String urlOfTimeApi() {
		if (environment.containsProperty(PROPERTY_TIME_API)) {
			return environment.getProperty(PROPERTY_TIME_API);
		} else {
			return DEFAULT_TIME_API_URL;
		}
	}
}
