package de.is24.cloud.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static de.is24.cloud.utils.HostUtils.getHostname;


@XmlRootElement(name = "currentDate")
public class CurrentDate {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");

	private final String date;
	private final String time;
	private final String origin;

	public CurrentDate() {
		final Date currentDate = new Date();
		date = DATE_FORMAT.format(currentDate);
		time = TIME_FORMAT.format(currentDate);
		origin = getHostname();
	}

	@XmlElement
	public String getDate() {
		return date;
	}

	@XmlElement
	public String getTime() {
		return time;
	}

	@XmlElement
	public String getOrigin() {
		return origin;
	}
}
