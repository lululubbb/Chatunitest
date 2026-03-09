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

class CSVRecord_11_2Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        values = new String[] {"val0", "val1", "val2"};
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping() {
        mapping.clear();
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(targetMap);

        assertSame(targetMap, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingAllValidIndices() {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(targetMap);

        assertSame(targetMap, result);
        assertEquals(3, result.size());
        assertEquals("val0", result.get("key0"));
        assertEquals("val1", result.get("key1"));
        assertEquals("val2", result.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingSomeInvalidIndices() {
        mapping.put("key0", 0);
        mapping.put("key1", 10); // invalid index, out of values range
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(targetMap);

        assertSame(targetMap, result);
        assertEquals(2, result.size());
        assertEquals("val0", result.get("key0"));
        assertFalse(result.containsKey("key1")); // key1 should not be present
        assertEquals("val2", result.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyValuesArray() {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(new String[0], mapping, null, 0L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(targetMap);

        assertSame(targetMap, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_privateMethodInvocationUsingReflection() throws Exception {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(values, mapping, null, 0L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> targetMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(1, result.size());
        assertEquals("val0", result.get("key0"));
    }
}