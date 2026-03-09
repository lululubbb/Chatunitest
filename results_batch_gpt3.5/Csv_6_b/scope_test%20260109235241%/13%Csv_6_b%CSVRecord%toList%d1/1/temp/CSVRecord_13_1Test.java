package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_13_1Test {

    private CSVRecord csvRecordWithValues;
    private CSVRecord csvRecordEmptyValues;

    @BeforeEach
    void setUp() {
        String[] values = new String[] { "one", "two", "three" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        csvRecordWithValues = new CSVRecord(values, mapping, "comment", 1L);

        String[] emptyValues = new String[0];
        csvRecordEmptyValues = new CSVRecord(emptyValues, new HashMap<>(), null, 0L);
    }

    @Test
    @Timeout(8000)
    void testToListReturnsCorrectList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(csvRecordWithValues);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList("one", "two", "three"), result);
    }

    @Test
    @Timeout(8000)
    void testToListReturnsEmptyListForEmptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(csvRecordEmptyValues);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(Arrays.asList(), result);
    }
}