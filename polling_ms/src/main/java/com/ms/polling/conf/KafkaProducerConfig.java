package com.ms.polling.conf;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
// Add the following import statements for custom serializers
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.Message;

import com.ms.polling.serializer.MessageDeserializer;
import com.ms.polling.serializer.MessageSerializer;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public Map<String, Object> producerConfigs() {
	    Map<String, Object> props = new HashMap<>();

	    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class);
	    props.put(ProducerConfig.ACKS_CONFIG, "all");
	    props.put(ProducerConfig.RETRIES_CONFIG, 10);
	    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
	    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
	    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

	    // Configurazione dei parametri di batching:
	    // batch.size: la dimensione massima del batch (espressa in byte). 
	    // Ad esempio, se si vuole impostare la dimensione del batch a 1MB, si può usare il valore 1024 * 1024.
	    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024 * 1024); // 1MB di dimensione del batch

	    // linger.ms: il tempo massimo di attesa (espresso in millisecondi) per l'arrivo di altri messaggi prima di inviare un batch.
	    // Ad esempio, se si vuole impostare il tempo di attesa a 100ms, si può usare il valore 100.
	    props.put(ProducerConfig.LINGER_MS_CONFIG, 100); // 100ms di tempo di attesa

	    // max.in.flight.requests.per.connection: il numero massimo di richieste non confermate che il producer può avere in volo.
	    // Ad esempio, se si vuole impostare il numero massimo di richieste in volo a 5, si può usare il valore 5.
	    props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5); // 5 richieste massime in volo per connessione

	    return props;
	}


	// Define custom serializers for Message<String>
	@Bean
	public Serializer<Message<String>> messageSerializer() {
		return new MessageSerializer();
	}

	@Bean
	public Deserializer<Message<String>> messageDeserializer() {
		return new MessageDeserializer();
	}

	@Bean
	public ProducerFactory<String, Message<String>> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, Message<String>> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
