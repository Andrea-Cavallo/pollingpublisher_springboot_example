package com.ms.polling.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ms.polling.domain.Outbox;
import com.ms.polling.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxService {

	private final OutboxRepository outboxRepository;

	public List<Outbox> findOutboxesAfterDate(Instant date) {
		return outboxRepository.findByCreatedAtAfter(date);
	}

	public List<Outbox> findAll() {
		return (List<Outbox>) outboxRepository.findAll();
	}

	public void delete(Outbox outbox) {
		outboxRepository.delete(outbox);
	}

}
