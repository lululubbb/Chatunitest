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

class CSVRecord_11_6Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        values = new String[]{"val0", "val1", "val2"};
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapAndValidMapping() {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertEquals("val0", map.get("key0"));
        assertEquals("val1", map.get("key1"));
        assertEquals("val2", map.get("key2"));
        assertEquals(3, map.size());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingIndexOutOfBounds() {
        mapping.put("key0", 0);
        mapping.put("key1", 3); // out of bounds index
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertEquals("val0", map.get("key0"));
        assertFalse(map.containsKey("key1"));
        assertEquals(1, map.size());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping() {
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNullValuesArray() throws Exception {
        // Use reflection to create CSVRecord with null values array to test null handling
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance((Object) null, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();
        // The method putIn should handle null values gracefully (no NPE)
        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withPrivateMethodInvocationUsingReflection() throws Exception {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertSame(map, result);
        assertEquals("val0", map.get("key0"));
    }
}