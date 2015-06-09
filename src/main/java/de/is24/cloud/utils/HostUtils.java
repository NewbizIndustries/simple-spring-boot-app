package de.is24.cloud.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;


public final class HostUtils {
	private HostUtils() {
	}

	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}
}
