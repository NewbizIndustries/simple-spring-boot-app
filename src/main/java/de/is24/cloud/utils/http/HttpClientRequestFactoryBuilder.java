package de.is24.cloud.utils.http;

import lombok.extern.slf4j.Slf4j;

import java.net.Proxy;

import java.util.Optional;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;


@Slf4j
public class HttpClientRequestFactoryBuilder {
	private Integer timeout = 0;
	private Optional<Proxy> proxy = Optional.empty();

	public HttpClientRequestFactoryBuilder withTimeout(final int timeout) {
		this.timeout = timeout;
		return this;
	}

	public HttpClientRequestFactoryBuilder withProxy(final Optional<Proxy> proxy) {
		this.proxy = proxy;
		return this;
	}

	public ClientHttpRequestFactory build() {
		return applySettings(new SimpleClientHttpRequestFactory());
	}

	public ClientHttpRequestFactory buildUnsafe() {
		return applySettings(new UnsafeSimpleClientHttpRequestFactory());
	}

	private ClientHttpRequestFactory applySettings(final SimpleClientHttpRequestFactory clientHttpRequestFactory) {
		clientHttpRequestFactory.setConnectTimeout(timeout);
		clientHttpRequestFactory.setReadTimeout(timeout);
		if (proxy.isPresent()) {
			log.info("using proxy server: {}", proxy.get().address());
			clientHttpRequestFactory.setProxy(proxy.get());
		} else {
			log.info("no proxy server configured");
		}
		return clientHttpRequestFactory;
	}
}
