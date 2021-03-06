package de.is24.cloud.resources;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.is24.cloud.services.ConfigService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;


@RestController
@Slf4j
public class ConfigResource {
	@Autowired
	private ConfigService configService;

	@RequestMapping(value = "/timeApiUrl", method = RequestMethod.GET)
	public ResponseEntity<String> getTimeApiUrl() {
		log.info("resource /timeApiUrl requested, GET");
		return new ResponseEntity<>(configService.getTimeApiUrl(), OK);
	}

	@RequestMapping(value = "/timeApiUrl", method = RequestMethod.PUT)
	public ResponseEntity<String> setTimeApiUrl(@RequestBody
		final String newTimeApiUrl) {
		log.info("resource /timeApiUrl requested, PUT");
		configService.setTimeApiUrl(newTimeApiUrl);
		return new ResponseEntity<>(configService.getTimeApiUrl(), CREATED);
	}
}
