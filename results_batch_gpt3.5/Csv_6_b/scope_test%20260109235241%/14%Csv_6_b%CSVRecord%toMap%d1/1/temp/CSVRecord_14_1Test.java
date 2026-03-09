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
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] {"value1", "value2"};
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    public void testToMapPopulated() {
        Map<String, String> map = csvRecord.toMap();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToMapEmptyValues() throws Exception {
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 2L);

        // Use reflection to invoke package-private putIn method with empty map
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(emptyRecord, new HashMap<String, String>());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testToMapWithNullMapping() throws Exception {
        CSVRecord nullMappingRecord = new CSVRecord(values, null, null, 3L);

        // Use reflection to invoke package-private putIn method with new map
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(nullMappingRecord, new HashMap<String, String>());
        assertNotNull(result);
        // When mapping is null, no entries should be added
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testToMapWithPartialMapping() {
        Map<String, Integer> partialMapping = new HashMap<>();
        partialMapping.put("header1", 0);
        CSVRecord partialRecord = new CSVRecord(values, partialMapping, null, 4L);
        Map<String, String> map = partialRecord.toMap();
        assertNotNull(map);
        assertEquals(1, map.size());
        assertEquals("value1", map.get("header1"));
    }
}