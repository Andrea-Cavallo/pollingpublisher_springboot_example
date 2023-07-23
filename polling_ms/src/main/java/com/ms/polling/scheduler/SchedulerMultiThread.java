package com.ms.polling.scheduler;

import static com.ms.polling.utils.Constants.LOG_TOPIC;
import static com.ms.polling.utils.Constants.SOMETHING_WENT_WRONG;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.polling.domain.Outbox;
import com.ms.polling.exception.ProducerException;
import com.ms.polling.mapper.OutboxMapper;
import com.ms.polling.producer.OutboxProducer;
import com.ms.polling.service.LastQueryInstantConfigManger;
import com.ms.polling.service.OutboxService;
import com.ms.polling.utils.SchedulerUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerMultiThread {

	private final OutboxProducer outboxProducer;
	private final ObjectMapper objectMapper;
	private final OutboxService outboxService;
	private final LastQueryInstantConfigManger configService;
	private final OutboxMapper outboxMapper;

	public void scheduledTask() {
		String corrId = SchedulerUtils.generateCorrelationId();

		Optional<Instant> lastInstantQueriedOpt = Optional.ofNullable(configService.getLastInstantQueried());
		List<Outbox> outboxes = lastInstantQueriedOpt.map(outboxService::findOutboxesAfterDate)
				.orElseGet(outboxService::findAll);
		configService.setLastInstantQueried(Instant.now());

		ExecutorService executor = Executors.newFixedThreadPool(10);

		List<CompletableFuture<Void>> futures = outboxes.stream().filter(Objects::nonNull)
				.map(outbox -> CompletableFuture.runAsync(() -> {
					try {
						var serializedOutbox = objectMapper.writeValueAsString(outbox);
						log.debug(LOG_TOPIC, serializedOutbox);
						outboxProducer.fire(outboxMapper.toOutboxEvent(outbox), corrId);
					} catch (Exception e) {
						throw new ProducerException(SOMETHING_WENT_WRONG, e);
					}
				}, executor)).collect(Collectors.toList());

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();

		executor.shutdown();
	}

}
