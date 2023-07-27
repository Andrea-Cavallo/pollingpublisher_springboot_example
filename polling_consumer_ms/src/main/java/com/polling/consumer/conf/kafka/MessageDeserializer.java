package com.polling.consumer.conf.kafka;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polling.consumer.exception.DeserializeException;
import com.polling.consumer.model.MyMessage;

public class MessageDeserializer implements Deserializer<MyMessage> {
    private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
    public MyMessage deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, MyMessage.class);
        } catch (Exception e) {
            throw new DeserializeException("Error deserializing My MESSAGE!!", e);
        }
    }
}
