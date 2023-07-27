package com.polling.consumer.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polling.consumer.model.Outbox;
import com.polling.consumer.model.User;
import com.polling.consumer.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private final UserRepository userRepository;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public Optional<User> saveUserFromOutbox(Outbox outbox) {
		try {
			String payload = outbox.getPayload();
			User user = fromPayloadToUser(payload);
			userRepository.save(user);
			logger.info("User saved successfully with id: {}", user.getId());
			return Optional.of(user);
		} catch (IOException e) {
			logger.error("Error while converting payload to User object: {}", outbox.getPayload(), e);
			return Optional.empty();
		}
	}

	public static User fromPayloadToUser(String payload) throws JsonProcessingException {

		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(payload,
				User.class);

	}

}
