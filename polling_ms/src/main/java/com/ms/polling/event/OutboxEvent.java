package com.ms.polling.event;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@EqualsAndHashCode
public class OutboxEvent {
	private Long id;

	private Long aggregateId;

	private String aggregateType;

	private String eventType;

	private Integer version;

	private String payload;

	private Boolean published;

	private Instant createdAt;
}
