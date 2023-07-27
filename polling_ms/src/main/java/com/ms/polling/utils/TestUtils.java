package com.ms.polling.utils;

import static com.ms.polling.utils.Constants.OUTBOX_CREATED;
import static com.ms.polling.utils.Constants.PAYLOAD_STRING;
import static com.ms.polling.utils.Constants.USER;

import java.time.Instant;

import com.ms.polling.domain.Outbox;
import com.ms.polling.event.OutboxEvent;

public class TestUtils {

	private TestUtils() {
	}

	public static OutboxEvent createOutboxEvent() {
		OutboxEvent outbox = new OutboxEvent();
		outbox.setAggregateId(123L);
		outbox.setAggregateType(USER);
		outbox.setCreatedAt(Instant.now());
		outbox.setEventType(OUTBOX_CREATED);
		outbox.setId(1L);
		outbox.setPayload(PAYLOAD_STRING);
		outbox.setPublished(false);
		outbox.setVersion(1);
		return outbox;
	}

	public static String createOutboxEventJson() {
		return "{\r\n" + "    \"payload\": {\r\n" + "        \"outbox\": {\r\n"
				+ "            \"aggregateId\": 123,\r\n" + "            \"aggregateType\": \"USER\",\r\n"
				+ "            \"createdAt\": \"2023-07-23T08:30:00Z\",\r\n"
				+ "            \"eventType\": \"OUTBOX_CREATED\",\r\n" + "            \"id\": 1,\r\n"
				+ "            \"payload\": \"{\\\"id\\\":27,\\\"name\\\":\\\"Andrea\\\",\\\"surname\\\":\\\"Cavallo\\\",\\\"orders\\\":null}\",\r\n"
				+ "            \"published\": false,\r\n" + "            \"version\": 1\r\n" + "        }\r\n"
				+ "    },\r\n" + "    \"topicName\": \"POLLING.CDC-OUTBOX.V1\"\r\n" + "}\r\n" + "";
	}

	public static Outbox createOutbox() {
		Outbox outbox = new Outbox();
		outbox.setAggregateId(123L);
		outbox.setAggregateType(USER);
		outbox.setCreatedAt(Instant.now());
		outbox.setEventType(OUTBOX_CREATED);
		outbox.setId(1L);
		outbox.setPayload(PAYLOAD_STRING);
		outbox.setPublished(false);
		outbox.setVersion(1);
		return outbox;

	}

}
