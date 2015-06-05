package hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "currentDate")
public class CurrentDate {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");

	private String date;
	private String time;

	public CurrentDate() {
		final Date currentDate = new Date();
		date=DATE_FORMAT.format(currentDate);
		time=TIME_FORMAT.format(currentDate);
	}

	@XmlElement
	public String getDate() {
		return date;
	}

	@XmlElement
	public String getTime() {
		return time;
	}
}
