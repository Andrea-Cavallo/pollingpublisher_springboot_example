package com.ms.polling.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ms.polling.domain.Outbox;

@Repository
public interface OutboxRepository extends CrudRepository<Outbox, Long> {

	Optional<Outbox> findByAggregateId(Long aggregateId);

	List<Outbox> findByCreatedAtAfter(Instant createdAt);
}
