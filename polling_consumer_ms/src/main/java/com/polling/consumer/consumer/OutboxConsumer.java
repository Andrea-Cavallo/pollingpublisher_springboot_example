package com.polling.consumer.consumer;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polling.consumer.model.MyMessage;
import com.polling.consumer.model.Outbox;
import com.polling.consumer.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OutboxConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OutboxConsumer.class);
	private final UserService userService;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RetryTemplate retryTemplate;

	public OutboxConsumer(UserService userService, RetryTemplate retryTemplate) {
		this.userService = userService;
		this.retryTemplate = retryTemplate;
	}

	@KafkaListener(topics = "POLLING.CDC-OUTBOX.V1", containerFactory = "outboxKafkaListenerContainerFactory", groupId = "outbox-service")
	public void consume(ConsumerRecord<String, MyMessage> consumerRecord, Acknowledgment acknowledgment) {
		if (consumerRecord == null || consumerRecord.value() == null) {
			logger.error("Received null consumer record or consumer record value from Kafka");
			return;
		}
		logger.info("Received message from topic 'POLLING.CDC-OUTBOX.V1': {}", consumerRecord.value());
		retryTemplate.execute(retryContext -> {
			try {
				MyMessage myMess = consumerRecord.value();
				if (myMess.getPayload() == null || myMess.getHeaders() == null) {
					logger.error("Received null payload or headers in MyMessage: {}", myMess);
					return null;
				}
				log.info("il MESSAGE è {}", myMess.getPayload());
				log.info("Headers sono {}", myMess.getHeaders().toString());
				JsonNode jsonNode = objectMapper.readTree(myMess.getPayload());
				JsonNode outboxPayload = jsonNode.get("outbox");

				Outbox outbox = objectMapper.treeToValue(outboxPayload, Outbox.class);
				if (outbox == null) {
					logger.error("Error while converting payload to Outbox object: {}", myMess.getPayload());
					return null;
				}
				log.info("OUTBOXXXX è {}", outbox.toString());

				userService.saveUserFromOutbox(outbox);
				acknowledgment.acknowledge();
			} catch (IOException e) {
				logger.error("Error while converting message to Outbox object: {}", consumerRecord.value(), e);
				throw new RuntimeException(e);
			}
			return null;
		});
	}

}
