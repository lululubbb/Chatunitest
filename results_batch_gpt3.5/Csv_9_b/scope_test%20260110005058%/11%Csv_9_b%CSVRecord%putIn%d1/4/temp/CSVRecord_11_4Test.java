package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecordPutInTest {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() throws Exception {
        // Prepare mapping with entries, some with valid index, some out of bounds
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        mapping.put("D", 5); // out of bounds index

        // Values array with length 3
        values = new String[] { "val0", "val1", "val2" };

        // Create CSVRecord instance via reflection since constructor is package-private
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class,
                long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance((Object) values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withValidAndInvalidIndices() throws Exception {
        Map<String, String> map = new HashMap<>();
        // Use reflection to get putIn method (package-private)
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        // Verify map contains keys with valid indices
        assertEquals("val0", result.get("A"));
        assertEquals("val1", result.get("B"));
        assertEquals("val2", result.get("C"));
        // Key with out of bounds index should not be present
        assertFalse(result.containsKey("D"));
        // The returned map is the same instance passed in
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping() throws Exception {
        // Create CSVRecord with empty mapping
        Map<String, Integer> emptyMapping = new HashMap<>();
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class,
                long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) values, emptyMapping, null, 2L);

        Map<String, String> map = new HashMap<>();
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(record, map);

        // Map should remain empty
        assertTrue(result.isEmpty());
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyValues() throws Exception {
        // Values array empty
        String[] emptyValues = new String[0];
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class,
                long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) emptyValues, mapping, null, 3L);

        Map<String, String> map = new HashMap<>();
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(record, map);

        // No keys should be added because all indices are out of bounds
        assertTrue(result.isEmpty());
        assertSame(map, result);
    }

}