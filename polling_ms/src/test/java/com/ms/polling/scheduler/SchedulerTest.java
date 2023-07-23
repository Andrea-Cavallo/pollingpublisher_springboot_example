//package com.ms.polling.scheduler;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ms.polling.domain.Outbox;
//import com.ms.polling.event.OutboxEvent;
//import com.ms.polling.mapper.OutboxMapper;
//import com.ms.polling.producer.OutboxProducer;
//import com.ms.polling.service.LastQueryInstantConfigManger;
//import com.ms.polling.service.OutboxService;
//import com.ms.polling.utils.SchedulerUtils;
//
//
// class SchedulerTest {
//
//    @Mock
//    private OutboxProducer outboxProducer;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private OutboxService outboxService;
//
//    @Mock
//    private LastQueryInstantConfigManger configService;
//
//    @Mock
//    private OutboxMapper outboxMapper;
//
//    @InjectMocks
//    private Scheduler scheduler;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//    
//    private final String CORR_ID ="a0f56c57-d34e-4fc1-90e6-942d952b957";
//
//    @Test
//     void testScheduledTask() throws Exception {
//        Instant testInstant = Instant.parse("2023-07-01T12:00:00Z");
//
//
//        when(SchedulerUtils.generateCorrelationId()).thenReturn(CORR_ID);
//        when(configService.getLastInstantQueried()).thenReturn(testInstant);
//
//        List<Outbox> outboxesAfterDate = new ArrayList<>();
//        when(outboxService.findOutboxesAfterDate(testInstant)).thenReturn(outboxesAfterDate);
//        Outbox outbox = new Outbox();
//        when(outboxMapper.toOutboxEvent(outbox)).thenReturn(new OutboxEvent());
//
//        scheduler.scheduledTask();
//
//        // Verifiche sui metodi chiamati
//        verify(configService, times(1)).getLastInstantQueried();
//        verify(configService, times(1)).setLastInstantQueried(any());
//        verify(outboxService, times(1)).findOutboxesAfterDate(testInstant);
//        verify(outboxService, times(1)).findAll(); 
//        verify(outboxMapper, times(1)).toOutboxEvent(outbox);
//
//        verify(outboxProducer, times(1)).fire(any(OutboxEvent.class), eq(CORR_ID));}
//}

