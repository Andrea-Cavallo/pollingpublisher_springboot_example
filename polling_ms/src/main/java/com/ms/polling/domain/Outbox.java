package com.ms.polling.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Entity(name = "Outbox")

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class Outbox {

	@Id
	private Long id;

	@Column(name = "aggregate_id", nullable = false)
	private Long aggregateId;

	@Column(name = "aggregate_type", nullable = false)
	private String aggregateType;

	@Column(name = "event_type", nullable = false)
	private String eventType;

	@Column(name = "version")
	private Integer version;

	@Column(name = "payload", nullable = false)
	private String payload;

	@Column(name = "published", nullable = false)
	private Boolean published;

	@Column(name = "created_at", updatable = false, nullable = false)
	private Instant createdAt;
}
