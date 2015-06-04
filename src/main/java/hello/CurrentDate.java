package hello;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "currentDate")
public class CurrentDate {
	private final Date date = new Date();
	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	private final DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

	@XmlElement
	public String getDate() {
		return dateFormat.format(date);
	}

	@XmlElement
	public String getTime() {
		return timeFormat.format(date);
	}
}
