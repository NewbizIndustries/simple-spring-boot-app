package de.is24.cloud.configuration;

import com.amazonaws.ClientConfiguration;

import com.amazonaws.auth.AWSCredentialsProvider;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import de.is24.cloud.messaging.SqsQueue;

import static java.lang.String.format;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import static de.is24.cloud.configuration.AppEnvironment.getStackName;
import static de.is24.cloud.configuration.AppEnvironment.isDevProfile;
import static de.is24.cloud.configuration.AppEnvironment.sqsHost;
import static de.is24.cloud.configuration.AppEnvironment.sqsPort;


@Configuration
@Import(AwsConfig.class)
@Slf4j
public class MessagingConfig {
	private static final Regions REGION = Regions.EU_WEST_1;
	private static final String SQS_SERVICE_NAME = "sqs";
	private static final String INSTANCE_QUEUE = "instances";

	@Autowired
	private Environment environment;
	@Autowired
	private AWSCredentialsProvider awsCredentialsProvider;
	@Autowired
	private ClientConfiguration clientConfiguration;

	@Bean
	public AmazonSQS amazonSQSClient() {
		final AmazonSQSClient amazonSQSClient = new AmazonSQSClient(awsCredentialsProvider, clientConfiguration);
		if (isDevProfile(environment)) {
			final String endpoint = format("http://%s:%s", sqsHost(environment), sqsPort(environment));
			log.info("using endpoint {}", endpoint);
			amazonSQSClient.setEndpoint(endpoint, SQS_SERVICE_NAME, EMPTY);
		} else {
			amazonSQSClient.setRegion(Region.getRegion(REGION));
		}
		return amazonSQSClient;
	}

	@Bean
	public SqsQueue instanceQueue() {
		return getSqsQueue(INSTANCE_QUEUE);
	}

	private SqsQueue getSqsQueue(final String queueName) {
		final String url = findOrCreate(queueName);
		return new SqsQueue(url, amazonSQSClient());
	}

	private String findOrCreate(final String queueName) {
		final String sqsPrefix;
		if (isDevProfile(environment)) {
			sqsPrefix = EMPTY;
		} else {
			sqsPrefix = getStackName(environment).get() + "-";
		}

		final String awsQueueName = sqsPrefix + queueName;

		log.info("find or create sqs queue: {}", awsQueueName);

		final CreateQueueRequest createQueueRequest = new CreateQueueRequest(awsQueueName);
		final String queueUrl = amazonSQSClient().createQueue(createQueueRequest).getQueueUrl();
		log.info("sqs queue url: {}", queueUrl);
		return queueUrl;
	}
}
