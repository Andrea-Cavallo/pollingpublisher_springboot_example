package com.ms.polling.producer;

import static com.ms.polling.utils.Constants.CORRELATION_ID;
import static com.ms.polling.utils.Constants.OUTBOX;
import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1;
import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1_RET;
import static com.ms.polling.utils.Constants.TOPIC_NAME;

import java.util.HashMap;

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

	@Retryable(value = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000))
	public void fire(OutboxEvent outbox, String correlationId) throws JsonProcessingException {
		outbox.setPublished(true);
		HashMap<String, Object> mapOf = new HashMap<>();
		mapOf.put(OUTBOX, outbox);
		mapOf.put(TOPIC_NAME, POLLING_CDC_OUTBOX_V1);
		var json = objectMapper.writeValueAsString(mapOf);
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send(POLLING_CDC_OUTBOX_V1, message);
	}

	@Recover
	public void recover(Exception t, OutboxEvent outbox, String correlationId) throws JsonProcessingException {
		outbox.setPublished(false);
		HashMap<String, Object> mapOf = new HashMap<>();
		mapOf.put(OUTBOX, outbox);
		mapOf.put(TOPIC_NAME, POLLING_CDC_OUTBOX_V1_RET);
		mapOf.put("exception", t.getMessage());
		var json = objectMapper.writeValueAsString(mapOf);
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send(POLLING_CDC_OUTBOX_V1_RET, message);
	}

	public void deadLetter(Exception t, OutboxEvent outbox, String correlationId) {
		outbox.setPublished(false);
		HashMap<String, Object> mapOf = new HashMap<>();
		mapOf.put(OUTBOX, outbox);
		mapOf.put(TOPIC_NAME, "POLLING.CDC-OUTBOX.V1.DEAD-LETTER-TOPIC");
		mapOf.put("exception", t.getMessage());
		String json = "";
		try {
			json = objectMapper.writeValueAsString(mapOf);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send("POLLING.CDC-OUTBOX.V1.DEAD-LETTER-TOPIC", message);
	}
}
