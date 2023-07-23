package com.ms.polling.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class LastQueryInstantConfigMangerTest {

	@InjectMocks
	private LastQueryInstantConfigManger configManager;

	@BeforeEach
	public void setup() {
		configManager = new LastQueryInstantConfigManger();
	}

	@Test
	@DisplayName("GIVEN a function to check if a query was called" 
				+ "WHEN no queries where invoked"
			    + "THEN the last instant queried is null")
	void testGetLastInstantQueried_DefaultValue() {
		assertNull(configManager.getLastInstantQueried());
	}

	@Test
	@DisplayName("GIVEN a function to check if a query was called" 
				+ "WHEN a queries where invoked"
			    + "THEN the last instant queried is not null, and setted with instant.now")
	void testSetLastInstantQueried() {
		Instant testInstant = Instant.now();
		configManager.setLastInstantQueried(testInstant);
		assertEquals(testInstant, configManager.getLastInstantQueried());
	}

}
