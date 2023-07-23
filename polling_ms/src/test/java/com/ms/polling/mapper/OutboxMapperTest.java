package com.ms.polling.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ms.polling.domain.Outbox;
import com.ms.polling.event.OutboxEvent;
import com.ms.polling.utils.TestUtils;
class OutboxMapperTest {



	    @Test
	    @DisplayName("GIVEN an outbox entity"+
	    		"WHEN the outboxMapper function is called"
	    		+ "THEN return the outboxEvent")
	     void testToOutboxEvent() {
	        Outbox outbox = TestUtils.createOutbox();
	        OutboxMapper outboxMapper = new OutboxMapper();

	        OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(outbox);

	        assertEquals(outbox.getAggregateId(), outboxEvent.getAggregateId());
	        assertEquals(outbox.getAggregateType(), outboxEvent.getAggregateType());
	        assertEquals(outbox.getCreatedAt(), outboxEvent.getCreatedAt());
	        assertEquals(outbox.getEventType(), outboxEvent.getEventType());
	        assertEquals(outbox.getId(), outboxEvent.getId());
	        assertEquals(outbox.getPayload(), outboxEvent.getPayload());
	        assertEquals(outbox.getPublished(), outboxEvent.getPublished());
	        assertEquals(outbox.getVersion(), outboxEvent.getVersion());
	    }


	

}
