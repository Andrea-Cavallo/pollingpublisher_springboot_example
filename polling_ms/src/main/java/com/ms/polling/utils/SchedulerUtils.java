package com.ms.polling.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SchedulerUtils {

	private SchedulerUtils() {
	}

	/**
	 * Suddivide una lista di elementi in sottoliste di dimensione specificata. Il
	 * metodo fornito rappresenta un'alternativa all'utilizzo della libreria di
	 * Google com.google.common.collect.Lists.partition(List<?> list, int size) per
	 * suddividere una lista in sottoliste di dimensione specificata.
	 * 
	 * @param list La lista di elementi da suddividere.
	 * @param size La dimensione massima delle sottoliste.
	 * @param <T>  Il tipo degli elementi nella lista.
	 * @return Una lista contenente le sottoliste di elementi suddivisi.
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {
		List<List<T>> partitions = new ArrayList<>();
		for (int i = 0; i < list.size(); i += size) {
			partitions.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
		}
		return partitions;
	}

	public static String generateCorrelationId() {
		return UUID.randomUUID().toString();

	}
}
