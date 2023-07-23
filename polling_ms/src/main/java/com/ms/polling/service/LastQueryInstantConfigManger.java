package com.ms.polling.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

@Service
public class LastQueryInstantConfigManger {

	private Instant lastInstantQueried;

	public Instant getLastInstantQueried() {
		return lastInstantQueried;
	}

	/**
	 * la classe si occupa di gestire un'informazione relativa all'ultimo istante di
	 * interrogazione - viene settato se e solo se è già stata fatta una query
	 * 
	 * @param lastInstantQueried
	 */
	public void setLastInstantQueried(Instant lastInstantQueried) {
		this.lastInstantQueried = lastInstantQueried;
	}
}
