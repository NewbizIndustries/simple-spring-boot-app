package de.is24.cloud.messaging;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import java.util.List;
import java.util.function.Function;


@Slf4j
public class SqsQueue {
	private static final int MAX_NUMBER_OF_MESSAGES = 10;

	private final String queueUrl;
	private final AmazonSQS amazonSQSClient;

	public SqsQueue(final String queueUrl, final AmazonSQS amazonSQSClient) {
		this.queueUrl = queueUrl;
		this.amazonSQSClient = amazonSQSClient;
	}

	public void send(final String message) {
		log.info("put message {} to queue {}", message, URI.create(queueUrl).getPath());
		amazonSQSClient.sendMessage(queueUrl, message);
	}

	public void processOne(final Function<String, Boolean> function) {
		process(function, 1);
	}

	public void processMany(final Function<String, Boolean> function) {
		process(function, MAX_NUMBER_OF_MESSAGES);
	}

	public void process(final Function<String, Boolean> function, final int numberOfMessages) {
		final ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.setMaxNumberOfMessages(numberOfMessages);

		final List<Message> messages = amazonSQSClient.receiveMessage(receiveMessageRequest).getMessages();
		if (!messages.isEmpty()) {
			log.info("message found");
			messages.stream() //
			.filter(message -> function.apply(message.getBody())) //
			.forEach(message -> amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle()));
		}
	}

	public int count() {
		return amazonSQSClient.receiveMessage(queueUrl).getMessages().size();
	}
}
