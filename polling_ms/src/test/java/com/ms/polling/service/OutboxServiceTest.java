package com.ms.polling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ms.polling.domain.Outbox;
import com.ms.polling.repository.OutboxRepository;
 class OutboxServiceTest {

	private static final String INSTANTE_DATA = "2023-07-01T12:00:00Z";

	@Mock
	private OutboxRepository outboxRepository;

	@InjectMocks
	private OutboxService outboxService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("GIVEN a function to find a List of Outbox after a given instant" + 
				"WHEN an instant is provided in input"
			+ "THEN returns the List of Outbox created after that instant")
	void testFindOutboxesAfterDate() {
		Instant testDate = Instant.parse(INSTANTE_DATA);
        Outbox outbox = new Outbox();
        List<Outbox> outboxList = Collections.singletonList(outbox);	
        when(outboxRepository.findByCreatedAtAfter(testDate)).thenReturn(outboxList);
		List<Outbox> result = outboxService.findOutboxesAfterDate(testDate);
		assertEquals(outboxList.size(), result.size());
	}

	@Test
	@DisplayName("GIVEN a function to find A List of Outbox " + 
			"WHEN the findAll is called"
		+ "THEN returns the list of Outbox")
	void testFindAll() {
		List<Outbox> outboxList = new ArrayList<>();
		outboxList.add(new Outbox());
		outboxList.add(new Outbox());

		when(outboxRepository.findAll()).thenReturn(outboxList);

		List<Outbox> result = outboxService.findAll();

		assertEquals(outboxList.size(), result.size());
	}

	@Test
	@DisplayName("GIVEN a function to delete an Outbox " + 
			"WHEN the delete is called"
		+ "THEN is executed 1 time")
	void testDelete() {
		Outbox outbox = new Outbox();
		outboxService.delete(outbox);
		verify(outboxRepository, times(1)).delete(outbox);
	}
}
