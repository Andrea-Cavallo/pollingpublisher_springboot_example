package com.ms.polling.mapper;

import org.springframework.stereotype.Component;

import com.ms.polling.domain.Outbox;
import com.ms.polling.event.OutboxEvent;

@Component
public class OutboxMapper {

	public OutboxEvent toOutboxEvent(Outbox outbox) {
		return OutboxEvent.builder().aggregateId(outbox.getAggregateId()).aggregateType(outbox.getAggregateType())
				.createdAt(outbox.getCreatedAt()).eventType(outbox.getEventType()).id(outbox.getId())
				.payload(outbox.getPayload()).published(outbox.getPublished()).version(outbox.getVersion()).build();
	}
}
