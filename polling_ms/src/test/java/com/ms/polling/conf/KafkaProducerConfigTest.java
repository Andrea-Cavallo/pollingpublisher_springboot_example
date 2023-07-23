package com.ms.polling.conf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

 class KafkaProducerConfigTest {

	@Test
	void testProducerConfigs() {
		KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();

		Map<String, Object> props = kafkaProducerConfig.producerConfigs();

		assertEquals("localhost:29092", props.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
		assertEquals(StringSerializer.class, props.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
		assertEquals(StringSerializer.class, props.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
		assertEquals("all", props.get(ProducerConfig.ACKS_CONFIG));
		assertEquals(10, props.get(ProducerConfig.RETRIES_CONFIG));
		assertEquals(1, props.get(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION));
		assertEquals(true, props.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG));
		assertEquals("snappy", props.get(ProducerConfig.COMPRESSION_TYPE_CONFIG));
		assertEquals(32 * 1024, props.get(ProducerConfig.BATCH_SIZE_CONFIG));
		assertEquals(1, props.get(ProducerConfig.LINGER_MS_CONFIG));
	}


}
