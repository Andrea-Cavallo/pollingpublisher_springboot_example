package com.ms.polling.scheduler;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ms.polling.domain.Outbox;
import com.ms.polling.event.OutboxEvent;
import com.ms.polling.mapper.OutboxMapper;
import com.ms.polling.producer.OutboxFireEvent;
import com.ms.polling.service.LastQueryInstantConfigManger;
import com.ms.polling.service.OutboxService;
import com.ms.polling.utils.SchedulerUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

	private final OutboxFireEvent outboxFireEvent;
	private final OutboxService outboxService;
	private final LastQueryInstantConfigManger configService;
	private final OutboxMapper outboxMapper;


	// @Scheduled(cron = "0 0 * * * *") // ogni ora
	@Scheduled(cron = "*/20 * * * * *") // ogni 20 sec
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void scheduledTask() {
	    var corrId = SchedulerUtils.generateCorrelationId();
	    Optional<Instant> lastInstantQueriedOpt = Optional.ofNullable(configService.getLastInstantQueried());
	    List<Outbox> outboxes = getOutboxList(lastInstantQueriedOpt);
	    configService.setLastInstantQueried(Instant.now());

	    List<OutboxEvent> outboxEvents = outboxes
	    		.stream().filter(Objects::nonNull)
	            .map(outboxMapper::toOutboxEvent)
	            .collect(Collectors.toList());
	    
	    if  (Boolean.FALSE.equals(isListOfEventsNullOrEmpty(outboxEvents))) {
	    	log.info("List of outboxEvents is {}", outboxEvents);
		    log.info("Events has been sended with corrId {}",corrId);
	        outboxFireEvent.fireAll(outboxEvents, corrId);
		    log.info("Events was fired");

	    }
	}

	@Cacheable("outboxes")
	private List<Outbox> getOutboxList(Optional<Instant> lastInstantQueriedOpt) {
		return lastInstantQueriedOpt
				.map(outboxService::findOutboxesAfterDate)
				.orElseGet(outboxService::findAll);
	}
	
	private Boolean isListOfEventsNullOrEmpty(List<OutboxEvent> outboxEvents) {
		if (Objects.isNull(outboxEvents) || outboxEvents.isEmpty()) {
	    	log.info("List of outboxEvents is empty or null {}", outboxEvents);
	        log.info("There is no new outbox events to send. Terminating task.");
	        return true;
	    }else 
	    	return false;
	}

}
