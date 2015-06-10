package de.is24.cloud.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;


@Slf4j
class UnsafeSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
	@Override
	protected void prepareConnection(final HttpURLConnection connection, final String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			try {
				((HttpsURLConnection) connection).setHostnameVerifier((s, sslSession) -> true);
				((HttpsURLConnection) connection).setSSLSocketFactory(initUnsafeSslContext().getSocketFactory());
			} catch (KeyManagementException | NoSuchAlgorithmException e) {
				log.error("can't prepare connection", e);
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
