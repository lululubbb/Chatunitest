package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_14_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[]{"value1", "value2"};
        // Use reflection to access package-private constructor
        java.lang.reflect.Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testToMap_withValuesAndMapping() {
        Map<String, String> map = csvRecord.toMap();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToMap_emptyValues() throws Exception {
        // Create CSVRecord with empty values array using reflection
        String[] emptyValues = new String[0];
        java.lang.reflect.Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord emptyRecord = constructor.newInstance(emptyValues, mapping, null, 2L);

        Map<String, String> map = emptyRecord.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testToMap_emptyMapping() throws Exception {
        // Create CSVRecord with empty mapping using reflection
        Map<String, Integer> emptyMapping = new HashMap<>();
        java.lang.reflect.Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance(values, emptyMapping, null, 3L);

        Map<String, String> map = record.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutIn_invokedByToMap() throws Exception {
        // Use reflection to access package-private putIn method
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertSame(map, result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToMap_nullValuesAndMapping() throws Exception {
        // Use reflection to create CSVRecord with null values and mapping via constructor
        java.lang.reflect.Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((String[]) null, (Map<String, Integer>) null, null, 4L);

        Map<String, String> map = record.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }
}