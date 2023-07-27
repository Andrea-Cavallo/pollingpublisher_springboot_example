package com.ms.polling.serializer;

import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.polling.exception.DeserializeException;

public class MessageDeserializer implements Deserializer<Message<String>> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	@Override
	public Message<String> deserialize(String topic, byte[] data) {
		try {
			return objectMapper.readValue(data, Message.class);
		} catch (Exception e) {
			throw new DeserializeException("Error deserializing Message", e);
		}
	}
}
