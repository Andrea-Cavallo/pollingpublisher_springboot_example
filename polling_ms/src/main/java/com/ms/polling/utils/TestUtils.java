package com.ms.polling.utils;

import static com.ms.polling.utils.Constants.OUTBOX_CREATED;
import static com.ms.polling.utils.Constants.PAYLOAD_STRING;
import static com.ms.polling.utils.Constants.USER;

import java.time.Instant;

import com.ms.polling.domain.Outbox;
import com.ms.polling.event.OutboxEvent;

public class TestUtils {
	


	private TestUtils() {}
	
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
	    return "{" +
	           "\"aggregateId\": 123," +
	           "\"aggregateType\": \"USER\"," +
	           "\"createdAt\": \"2023-07-23T08:30:00Z\"," +
	           "\"eventType\": \"OUTBOX_CREATED\"," +
	           "\"id\": 1," +
	           "\"payload\": \"{\\\"id\\\":27,\\\"name\\\":\\\"Andrea\\\",\\\"surname\\\":\\\"Cavallo\\\",\\\"orders\\\":null}\"," +
	           "\"published\": false," +
	           "\"version\": 1" +
	           "}";
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
