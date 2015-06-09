package de.is24.cloud.utils.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;


public class UnsafeX509ThrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] x509Certificates, final String s) throws CertificateException {
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	}
}
