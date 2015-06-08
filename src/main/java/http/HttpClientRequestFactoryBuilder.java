package http;

import java.net.Proxy;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;


public class HttpClientRequestFactoryBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientRequestFactoryBuilder.class);

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
			LOGGER.info("using proxy server: {}", proxy.get().address());
			clientHttpRequestFactory.setProxy(proxy.get());
		} else {
			LOGGER.info("no proxy server configured");
		}
		return clientHttpRequestFactory;
	}
}
