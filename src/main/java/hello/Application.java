package hello;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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


	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
