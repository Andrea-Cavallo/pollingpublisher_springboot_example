package com.ms.polling.utils;

public class Constants {

	private Constants() {
	}

	// log constants
	public static final String SOMETHING_WENT_WRONG = "Something went wrong during when trying to produce the topic";
	public static final String LOG_TOPIC = "questo è quel che invieremo come topic : {}";
	// nomi topic
	public static final String POLLING_CDC_OUTBOX_V1_RET = "POLLING.CDC-OUTBOX.V1.RET";
	public static final String POLLING_CDC_OUTBOX_V1 = "POLLING.CDC-OUTBOX.V1";
	public static final String TOPIC_NAME = "topicName";

	public static final String OUTBOX = "outbox";
	// costanti utili per i test
	public static final String CORRELATION_ID = "correlation-id";
	public static final String CORR_ID_VALUE = "a0f56c57-d34e-4fc1-90e6-942d952b957";
	public static final String PAYLOAD_STRING = "{\"id\":27,\"name\":\"Andrea\",\"surname\":\"Cavallo\",\"orders\":null}";
	public static final String USER = "USER";
	public static final String OUTBOX_CREATED = "OUTBOX_CREATED";
}
