package com.ms.polling.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

 class SchedulerUtilsTest {

    @Test
    @DisplayName("GIVEN a function to calculate a list of partitions"+
    		"WHEN a list of elements and the max number of partitions is provided"
    		+ "THEN return a List of element partiotioned")
     void testPartition() {
        List<Integer> inputList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        int partitionSize = 3;

        List<List<Integer>> result = SchedulerUtils.partition(inputList, partitionSize);
        assertEquals(4, result.size()); // numero delle sottoliste -> 4
        assertEquals(3, result.get(0).size()); // verifichiamo che ognuna sottolsita abbia il numero esatto di partizioni
        assertEquals(3, result.get(1).size());
        assertEquals(3, result.get(2).size());
        assertEquals(1, result.get(3).size());
        assertEquals(Arrays.asList(1, 2, 3), result.get(0)); // numero dei valori esatti della sottolista
        assertEquals(Arrays.asList(4, 5, 6), result.get(1));
        assertEquals(Arrays.asList(7, 8, 9), result.get(2));
        assertEquals(Arrays.asList(10), result.get(3));
    }

    @Test
    @DisplayName("GIVEN a function to calculate a generic correlation id"+
    		"WHEN the function is called"
    		+ "THEN return a generic String correlation-id")
     void testGenerateCorrelationId() {
        String correlationId = SchedulerUtils.generateCorrelationId();
        assertNotNull(correlationId);
        assertTrue(isValidUUID(correlationId));
    }

    /**
     * 
     * @param uuid
     * @return true se è veramente del formato UUID la Stringa sennò false
     */
    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

