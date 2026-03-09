package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_14_5Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] { "value1", "value2" };
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToMapEmptyValues() throws Exception {
        // Create CSVRecord with empty values array
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, null, 0L);
        Map<String, String> map = emptyRecord.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutInViaReflection() throws Exception {
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);
        Map<String, String> targetMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, targetMap);
        assertSame(targetMap, result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
    }

}