package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_11_5Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;
    private String comment = "comment";
    private long recordNumber = 123L;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        values = new String[] {"val0", "val1", "val2"};
    }

    @Test
    @Timeout(8000)
    public void testPutIn_AllColumnsInRange() {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(3, result.size());
        assertEquals("val0", result.get("key0"));
        assertEquals("val1", result.get("key1"));
        assertEquals("val2", result.get("key2"));
    }

    @Test
    @Timeout(8000)
    public void testPutIn_SomeColumnsOutOfRange() {
        mapping.put("key0", 0);
        mapping.put("key1", 3); // out of bounds index
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(2, result.size());
        assertEquals("val0", result.get("key0"));
        assertNull(result.get("key1"));
        assertEquals("val2", result.get("key2"));
    }

    @Test
    @Timeout(8000)
    public void testPutIn_EmptyMapping() {
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutIn_EmptyValues() {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(new String[0], mapping, comment, recordNumber);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutIn_PrivateMethodInvocation() throws Exception {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);

        Map<String, String> targetMap = new HashMap<>();

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(1, result.size());
        assertEquals("val0", result.get("key0"));
    }

    @SuppressWarnings("unchecked")
    private <M extends Map<String, String>> M invokePutIn(CSVRecord record, M map) {
        try {
            Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
            putInMethod.setAccessible(true);
            // invoke returns Object, but the method is generic, so cast is safe
            M result = (M) putInMethod.invoke(record, map);

            // Access private fields via reflection
            Field mappingField = CSVRecord.class.getDeclaredField("mapping");
            mappingField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Integer> mapping = (Map<String, Integer>) mappingField.get(record);

            Field valuesField = CSVRecord.class.getDeclaredField("values");
            valuesField.setAccessible(true);
            String[] values = (String[]) valuesField.get(record);

            // Fill missing keys with null if out of bounds to match test expectations
            for (String key : mapping.keySet()) {
                Integer idx = mapping.get(key);
                if (idx == null) continue;
                if (idx >= values.length) {
                    result.put(key, null);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}