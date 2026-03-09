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
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_14_5Test {

    private CSVRecord csvRecordWithMapping;
    private CSVRecord csvRecordWithoutMapping;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        String[] values = new String[] { "value1", "value2", "value3" };

        // CSVRecord constructor is package-private, so use reflection to create instances
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecordWithMapping = constructor.newInstance(values, mapping, "comment", 1L);

        // For coverage of empty mapping and empty values
        csvRecordWithoutMapping = constructor.newInstance(new String[0], new HashMap<>(), null, 2L);
    }

    @Test
    @Timeout(8000)
    public void testToMap_withMapping() {
        Map<String, String> map = csvRecordWithMapping.toMap();
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }

    @Test
    @Timeout(8000)
    public void testToMap_emptyValuesAndMapping() {
        Map<String, String> map = csvRecordWithoutMapping.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutIn_directInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecordWithMapping, map);

        assertSame(map, result);
        assertEquals(3, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
        assertEquals("value3", result.get("header3"));

        // test with empty mapping and values
        Map<String, String> emptyMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> emptyResult = (Map<String, String>) putInMethod.invoke(csvRecordWithoutMapping, emptyMap);

        assertSame(emptyMap, emptyResult);
        assertTrue(emptyResult.isEmpty());
    }
}