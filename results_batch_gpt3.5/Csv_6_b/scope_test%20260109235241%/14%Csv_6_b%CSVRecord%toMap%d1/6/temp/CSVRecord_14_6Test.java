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

public class CSVRecord_14_6Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
    }

    @Test
    @Timeout(8000)
    public void testToMap_withValuesAndMapping() {
        String[] values = new String[] {"value1", "value2"};
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testToMap_emptyValues() {
        String[] values = new String[0];
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testToMap_nullMapping() {
        String[] values = new String[] {"value1", "value2"};
        csvRecord = new CSVRecord(values, null, null, 1L);

        Map<String, String> result = csvRecord.toMap();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutInCalledViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] values = new String[] {"value1", "value2"};
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get("header1"));
        assertEquals("value2", result.get("header2"));
    }
}