package com.ms.polling.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ms.polling.domain.Outbox;
import com.ms.polling.repository.OutboxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {

	private final OutboxRepository outboxRepository;

	public List<Outbox> findOutboxesAfterDate(Instant date) {
		log.info("calling the findByCreateAtAfter ->{}", date.toString());
		return outboxRepository.findByCreatedAtAfter(date);
	}

	public List<Outbox> findAll() {
		log.info("calling the findall...... ");
		return (List<Outbox>) outboxRepository.findAll();
	}

	public void delete(Outbox outbox) {
		outboxRepository.delete(outbox);
	}

}
