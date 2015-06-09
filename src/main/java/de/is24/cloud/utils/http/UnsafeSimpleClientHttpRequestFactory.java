package de.is24.cloud.utils.http;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.client.SimpleClientHttpRequestFactory;


public class UnsafeSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnsafeSimpleClientHttpRequestFactory.class);

	@Override
	protected void prepareConnection(final HttpURLConnection connection, final String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			try {
				((HttpsURLConnection) connection).setHostnameVerifier((s, sslSession) -> true);
				((HttpsURLConnection) connection).setSSLSocketFactory(initUnsafeSslContext().getSocketFactory());
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				LOGGER.error("can't prepare connection", e);
			}
		}
		super.prepareConnection(connection, httpMethod);
	}

	private SSLContext initUnsafeSslContext() throws KeyManagementException, NoSuchAlgorithmException {
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new UnsafeX509ThrustManager() }, new SecureRandom());
		return sc;
	}
}
