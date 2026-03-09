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

public class CSVRecord_14_4Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
    }

    @Test
    @Timeout(8000)
    public void testToMap_withNormalValues() {
        values = new String[] { "valA", "valB", "valC" };
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("valA", result.get("A"));
        assertEquals("valB", result.get("B"));
        assertEquals("valC", result.get("C"));
    }

    @Test
    @Timeout(8000)
    public void testToMap_withNullValues() {
        values = new String[] { null, "valB", null };
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertNull(result.get("A"));
        assertEquals("valB", result.get("B"));
        assertNull(result.get("C"));
    }

    @Test
    @Timeout(8000)
    public void testToMap_withEmptyValues() {
        values = new String[0];
        csvRecord = new CSVRecord(values, new HashMap<>(), null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutInDirectlyUsingReflection() throws Exception {
        values = new String[] { "valA", "valB" };
        // Use a mapping that matches only keys "A" and "B"
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);

        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertSame(map, result);
        assertEquals(2, result.size());
        assertEquals("valA", result.get("A"));
        assertEquals("valB", result.get("B"));
    }
}