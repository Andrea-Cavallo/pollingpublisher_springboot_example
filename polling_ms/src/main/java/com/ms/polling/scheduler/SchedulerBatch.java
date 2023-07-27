package com.ms.polling.scheduler;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms.polling.domain.Outbox;
import com.ms.polling.event.OutboxEvent;
import com.ms.polling.mapper.OutboxMapper;
import com.ms.polling.producer.OutboxFireEvent;
import com.ms.polling.service.LastQueryInstantConfigManger;
import com.ms.polling.service.OutboxService;
import com.ms.polling.utils.SchedulerUtils;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class SchedulerBatch {

	private final OutboxFireEvent fireEvent;
	private final OutboxService outboxService;
	private final LastQueryInstantConfigManger configService;
	private final OutboxMapper outboxMapper;

	public void scheduledTask() {
		String corrId = SchedulerUtils.generateCorrelationId();

		Optional<Instant> lastInstantQueriedOpt = Optional.ofNullable(configService.getLastInstantQueried());
		List<Outbox> outboxes = getListOfOutbox(lastInstantQueriedOpt);
		configService.setLastInstantQueried(Instant.now());

		final int batchSize = 1000;
		List<List<Outbox>> batches = SchedulerUtils.partition(outboxes, batchSize);

		batches.stream().forEach(batch -> {
			List<OutboxEvent> outboxEvents = batch.stream().filter(Objects::nonNull).map(outboxMapper::toOutboxEvent)
					.collect(Collectors.toList());
			fireEvent.fireAll(outboxEvents, corrId);
		});
	}

	private List<Outbox> getListOfOutbox(Optional<Instant> lastInstantQueriedOpt) {
		return lastInstantQueriedOpt.map(outboxService::findOutboxesAfterDate).orElseGet(outboxService::findAll);
	}

}
