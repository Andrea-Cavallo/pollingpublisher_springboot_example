package com.ms.polling.producer;

import static com.ms.polling.utils.Constants.CORRELATION_ID;
import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1;
import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1_RET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.polling.event.OutboxEvent;
@Service
public class OutboxProducer {


	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private KafkaTemplate<String, Message<String>> kafkaTemplate;

	/**
	 * Sends the message contained in the Outbox to the Kafka topic. This method is
	 * annotated with {@code @Retryable} to indicate that it should be retried in
	 * case of exceptions. It sets the 'published' flag of the Outbox to true,
	 * serializes it to JSON, and sends it to the Kafka topic using the
	 * KafkaTemplate.
	 *
	 * @param outbox The Outbox containing the message to be sent.
	 * @throws JsonProcessingException If there is an issue with JSON processing.
	 */
	@Retryable(value = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000))
	public void fire(OutboxEvent outbox, String correlationId) throws JsonProcessingException {
		outbox.setPublished(true);
		var json = objectMapper.writeValueAsString(outbox);
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send(POLLING_CDC_OUTBOX_V1, message);
	}

	/**
	 * Recovers from an exception during message sending by retrying the message in
	 * a different Kafka topic. This method is annotated with {@code @Recover} to
	 * indicate that it is the recovery method for the retryable operation. It sets
	 * the 'published' flag of the Outbox to true, serializes it to JSON, and sends
	 * it to a retry Kafka topic using the KafkaTemplate.
	 *
	 * @param t      The exception that triggered the recovery.
	 * @param outbox The Outbox containing the message to be retried.
	 * @throws JsonProcessingException If there is an issue with JSON processing.
	 */
	@Recover
	public void recover(Exception t, OutboxEvent outbox, String correlationId) throws JsonProcessingException {
		outbox.setPublished(false);
		var json = objectMapper.writeValueAsString(outbox);
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send(POLLING_CDC_OUTBOX_V1_RET, message);
	}

}
