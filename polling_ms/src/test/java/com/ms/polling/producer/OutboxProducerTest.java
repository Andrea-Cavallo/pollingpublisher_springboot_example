//package com.ms.polling.producer;
//
//import static com.ms.polling.utils.Constants.CORRELATION_ID;
//import static com.ms.polling.utils.Constants.CORR_ID_VALUE;
//import static com.ms.polling.utils.Constants.OUTBOX;
//import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1;
//import static com.ms.polling.utils.Constants.POLLING_CDC_OUTBOX_V1_RET;
//import static com.ms.polling.utils.Constants.TOPIC_NAME;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.HashMap;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.retry.ExhaustedRetryException;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ms.polling.event.OutboxEvent;
//import com.ms.polling.utils.TestUtils;
//
// class OutboxProducerTest {
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private KafkaTemplate<String, Message<String>> kafkaTemplate;
//
//    @InjectMocks
//    private OutboxProducer outboxProducer;
//
//    @Captor
//    private ArgumentCaptor<Message<String>> messageCaptor;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
// 
//
//    @Test
//	@DisplayName("GIVEN a a producer function" + 
//			"WHEN an outbox event is provided and a correlation-id"
//		+ "THEN if there is no exception the event is fired in the main topic")
//     void testFire() throws JsonProcessingException {
//        OutboxEvent outboxEvent = TestUtils.createOutboxEvent();
//        String payload = mockTopic();
//        HashMap<String,Object> mapOf = new HashMap<>();
//        mapOf.put(OUTBOX, payload);
//        mapOf.put(TOPIC_NAME, POLLING_CDC_OUTBOX_V1);
//        // da sistemare perche' adesso si usa emssageBuilder
//        when(objectMapper.writeValueAsString(MessageBuilder.withPayload(mapOf).build())).thenReturn(payload);
//        outboxProducer.fire(outboxEvent, CORR_ID_VALUE);
//        assertEquals(true, outboxEvent.getPublished());
//        verify(kafkaTemplate, times(1)).send(eq(POLLING_CDC_OUTBOX_V1), messageCaptor.capture());
//        Message<String> capturedMessage = messageCaptor.getValue();
//        assertEquals(mapOf.get(OUTBOX), capturedMessage.getPayload());
//        assertEquals(CORR_ID_VALUE, capturedMessage.getHeaders().get(CORRELATION_ID));
//    }
//
//    @Test
//	@DisplayName("GIVEN a a producer function" + 
//			"WHEN an outbox event is provided and a correlation-id"
//		+ "THEN if there is any exception the event is fired in the retry topic")
//     void testRecover() throws JsonProcessingException {
//        OutboxEvent outboxEvent = TestUtils.createOutboxEvent();
//        String json = TestUtils.createOutboxEventJson();
//        when(objectMapper.writeValueAsString(outboxEvent)).thenReturn(json);
//        outboxProducer.recover(new ExhaustedRetryException("Test retry exception"), outboxEvent, CORR_ID_VALUE);
//        assertEquals(false, outboxEvent.getPublished());
//        verify(kafkaTemplate, times(1)).send(eq(POLLING_CDC_OUTBOX_V1_RET), messageCaptor.capture());
//        Message<String> capturedMessage = messageCaptor.getValue();
//        assertEquals(json, capturedMessage.getPayload());
//        assertEquals(CORR_ID_VALUE, capturedMessage.getHeaders().get(CORRELATION_ID));
//    }
//    
//
//    
//    private String mockTopic() {
//        String payload = "{\"topicName\":\"POLLING.CDC-OUTBOX.V1\",\"outbox\":{\"id\":2659656381702854399,\"aggregateId\":57,\"aggregateType\":\"User\",\"eventType\":\"USER_CREATED\",\"version\":1,\"payload\":\"{\\\"id\\\":57,\\\"name\\\":\\\"Mario\\\",\\\"surname\\\":\\\"Rossi\\\",\\\"orders\\\":null}\",\"published\":true,\"createdAt\":\"2023-07-24T06:52:45.637Z\"}}";
//        String headers = "{\"correlation-id\":\"31019453-a107-40e8-a123-861c8259a458\",\"id\":\"6e86c41d-93ab-4bb8-cd94-9aac5409df26\",\"timestamp\":1690184780249}";
//
//        return "{" +
//                "\"payload\":" + payload + "," +
//                "\"headers\":" + headers +
//                "}";
//    }
//
//}
//
