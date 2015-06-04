package hello;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class Application {
	@RequestMapping("/")
	public String home() {
		final StringBuilder sb = new StringBuilder();
		Arrays.asList("Willkommen ", "Bienvenue ", "Welcome ").forEach(sb::append);

		return "Great Greetings: " + sb.toString();
	}

	@RequestMapping(value = "/time", produces = "application/json")
	public ResponseEntity<CurrentDate> timestamp() {
		return new ResponseEntity<>(new CurrentDate(), HttpStatus.OK);
	}

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
