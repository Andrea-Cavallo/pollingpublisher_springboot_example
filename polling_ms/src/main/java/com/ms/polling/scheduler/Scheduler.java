package com.ms.polling.scheduler;

import static com.ms.polling.utils.Constants.LOG_TOPIC;
import static com.ms.polling.utils.Constants.SOMETHING_WENT_WRONG;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class Scheduler {

	private final OutboxProducer outboxProducer;
	private final ObjectMapper objectMapper;
	private final OutboxService outboxService;
	private final LastQueryInstantConfigManger configService;
	private final OutboxMapper outboxMapper;

	/**
	 * 
	 * Questo metodo viene utilizzato per pianificare un task che interroga un
	 * elenco di entità 'Outbox' adesso esagerato ogni 20 secondi per scenari di
	 * test.

	 * 
	 */
	// @Scheduled(cron = "0 0 * * * *") // ogni ora
	@Scheduled(cron = "*/20 * * * * *") // ogni 20 sec
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void scheduledTask() {
		var corrId = SchedulerUtils.generateCorrelationId();
		Optional<Instant> lastInstantQueriedOpt = Optional.ofNullable(configService.getLastInstantQueried());
		List<Outbox> outboxes = getOutboxList(lastInstantQueriedOpt);
		configService.setLastInstantQueried(Instant.now());
		outboxes.stream().filter(Objects::nonNull).forEach(outbox -> {
			try {
				var serializedOutbox = objectMapper.writeValueAsString(outbox);
				log.debug(LOG_TOPIC, serializedOutbox);
				outboxProducer.fire(outboxMapper.toOutboxEvent(outbox), corrId);
				// una volta inviato cancello outboxService.delete(outbox);
			} catch (Exception e) {
				throw new ProducerException(SOMETHING_WENT_WRONG, e);
			}
		});
	}

	/**
	 * * Recupera le entità 'Outbox' o dopo una data specifica, se presente, o tutte
	 * le entità disponibili. L'istante dell'ultima query viene quindi aggiornato
	 * all'orario corrente. Ogni
	 * 
	 * @param lastInstantQueriedOpt
	 * @return List<Outbox>
	 */
	private List<Outbox> getOutboxList(Optional<Instant> lastInstantQueriedOpt) {
		return lastInstantQueriedOpt.map(outboxService::findOutboxesAfterDate).orElseGet(outboxService::findAll);
	}

}
