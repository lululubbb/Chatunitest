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

public class CSVRecord_11_2Test {

    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare mapping and values for CSVRecord constructor
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);

        String[] values = new String[] { "val0", "val1", "val2" };

        // Use constructor: CSVRecord(final String[] values, final Map<String, Integer> mapping,
        // final String comment, final long recordNumber)
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withEmptyMap() throws Exception {
        Map<String, String> map = new HashMap<>();

        // Use reflection to access putIn method
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        // Check that the returned map is the same instance
        assertSame(map, result);

        // Check map content matches mapping keys and values array
        assertEquals(3, map.size());
        assertEquals("val0", map.get("A"));
        assertEquals("val1", map.get("B"));
        assertEquals("val2", map.get("C"));
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withPrePopulatedMap() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("X", "oldValue");

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        // The original entry should remain, and new entries added
        assertEquals(4, map.size());
        assertEquals("oldValue", map.get("X"));
        assertEquals("val0", map.get("A"));
        assertEquals("val1", map.get("B"));
        assertEquals("val2", map.get("C"));

        // Returned map is the same instance
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withEmptyMapping() throws Exception {
        // Create CSVRecord with empty mapping
        Map<String, Integer> emptyMapping = new HashMap<>();
        String[] values = new String[] { "v0", "v1" };
        CSVRecord record = new CSVRecord(values, emptyMapping, null, 0L);

        Map<String, String> map = new HashMap<>();

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(record, map);

        // Map should remain empty
        assertTrue(map.isEmpty());
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withMappingIndexOutOfBounds() throws Exception {
        // Setup mapping with index out of bounds of values array
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 5); // Out of bounds index

        String[] values = new String[] { "val0", "val1" };

        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> map = new HashMap<>();

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            putInMethod.invoke(record, map);
        });
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof ArrayIndexOutOfBoundsException);
    }
}