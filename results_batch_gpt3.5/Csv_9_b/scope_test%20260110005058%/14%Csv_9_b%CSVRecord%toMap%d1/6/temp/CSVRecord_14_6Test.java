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

class CSVRecord_14_6Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
    }

    @Test
    @Timeout(8000)
    void testToMap_withValuesAndMapping() {
        values = new String[] { "value1", "value2", "value3" };
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertEquals(3, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
        assertEquals("value3", result.get("header3"));
    }

    @Test
    @Timeout(8000)
    void testToMap_withEmptyValues() {
        values = new String[0];
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testToMap_withNullMapping() {
        values = new String[] { "value1" };
        csvRecord = new CSVRecord(values, null, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_privateMethod_behavior() throws Exception {
        values = new String[] { "v1", "v2" };
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertEquals(2, result.size());
        assertEquals("v1", result.get("header1"));
        assertEquals("v2", result.get("header2"));
    }
}