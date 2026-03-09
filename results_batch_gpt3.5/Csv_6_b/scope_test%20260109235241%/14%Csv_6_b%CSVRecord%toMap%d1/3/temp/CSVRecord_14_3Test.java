package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_14_3Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);
    }

    @Test
    @Timeout(8000)
    void testToMap_AllValuesPresent() {
        String[] values = new String[] {"value1", "value2", "value3"};
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
        assertEquals("value3", result.get("header3"));
    }

    @Test
    @Timeout(8000)
    void testToMap_EmptyValues() {
        String[] values = new String[0];
        csvRecord = new CSVRecord(values, new HashMap<>(), null, 2L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testToMap_NullValuesInArray() {
        String[] values = new String[] {"value1", null, "value3"};
        csvRecord = new CSVRecord(values, mapping, null, 3L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("header1"));
        assertNull(result.get("header2"));
        assertEquals("value3", result.get("header3"));
    }

    @Test
    @Timeout(8000)
    void testPrivatePutInMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> localMapping = new HashMap<>();
        localMapping.put("header1", 0);
        localMapping.put("header2", 1);
        csvRecord = new CSVRecord(values, localMapping, null, 4L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) putInMethod.invoke(csvRecord, new HashMap<String, String>());

        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals("v1", map.get("header1"));
        assertEquals("v2", map.get("header2"));
    }
}