package com.ms.polling.serializer;

import org.apache.kafka.common.serialization.Serializer;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.polling.exception.SerializeException;

public class MessageSerializer implements Serializer<Message<String>> {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String topic, Message<String> data) {
		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializeException("Error serializing Message", e);
		}
	}
}
