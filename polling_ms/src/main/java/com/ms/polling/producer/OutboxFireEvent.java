package com.ms.polling.producer;

import static com.ms.polling.utils.Constants.CORRELATION_ID;
import static com.ms.polling.utils.Constants.OUTBOX;
import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1;
import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1_RET;
import static com.ms.polling.utils.Constants.TOPIC_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ms.polling.exception.ProducerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxFireEvent  {

	private final ObjectMapper objectMapper;

	private final KafkaTemplate<String, Message<String>> kafkaTemplate;

	@Retryable(value = { Exception.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000))
	public void fireAll(List<OutboxEvent> outboxEvents, String correlationId) {
		List<String> failedMessages = processOutboxEvents(outboxEvents, correlationId);
		if (!failedMessages.isEmpty()) {
			log.error("some error occured during and was not published correlationId is {}", correlationId);
			throw new ProducerException("Some messages failed to be published: " + String.join(", ", failedMessages));
		}
	}

	private List<String> processOutboxEvents(List<OutboxEvent> outboxEvents, String correlationId) {
		List<String> exceptions = new ArrayList<>();
		outboxEvents.stream().forEach(outbox -> processSingleOutboxEvent(outbox, correlationId, exceptions));
		return exceptions;
	}

	private void processSingleOutboxEvent(OutboxEvent outbox, String correlationId, List<String> exceptions) {
		try {
			publishOutboxEvent(outbox, correlationId);
		} catch (Exception e) {
			handlePublishingException(outbox, correlationId, exceptions, e);
		}
	}

	private void publishOutboxEvent(OutboxEvent outbox, String correlationId) throws JsonProcessingException {
		outbox.setPublished(true);
		Map<String, Object> outboxMap = prepareOutboxMap(outbox);
		String json = objectMapper.writeValueAsString(outboxMap);
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send(POLLING_CDC_OUTBOX_V1, message);
	}

	private Map<String, Object> prepareOutboxMap(OutboxEvent outbox) {
		Map<String, Object> outboxMap = new HashMap<>();
		outboxMap.put(OUTBOX, outbox);
		outboxMap.put(TOPIC_NAME, POLLING_CDC_OUTBOX_V1);
		return outboxMap;
	}

	private void handlePublishingException(OutboxEvent outbox, String correlationId, List<String> exceptions,
			Exception e) {
		outbox.setPublished(false);
		exceptions.add(e.getMessage());
		tryRecover(e, outbox, correlationId);
	}

	private void tryRecover(Exception e, OutboxEvent outbox, String correlationId) {
		try {
			recover(e, outbox, correlationId);
		} catch (JsonProcessingException jsonException) {
			jsonException.printStackTrace();
		}
	}

	@Recover
	private void recover(Exception t, OutboxEvent outbox, String correlationId) throws JsonProcessingException {
		outbox.setPublished(false);
		HashMap<String, Object> mapOf = new HashMap<>();
		mapOf.put(OUTBOX, outbox);
		mapOf.put(TOPIC_NAME, POLLING_CDC_OUTBOX_V1_RET);
		mapOf.put("exception", t.getMessage());
		var json = objectMapper.writeValueAsString(mapOf);
		Message<String> message = MessageBuilder.withPayload(json).setHeader(CORRELATION_ID, correlationId).build();
		kafkaTemplate.send(POLLING_CDC_OUTBOX_V1_RET, message);
	}
}
