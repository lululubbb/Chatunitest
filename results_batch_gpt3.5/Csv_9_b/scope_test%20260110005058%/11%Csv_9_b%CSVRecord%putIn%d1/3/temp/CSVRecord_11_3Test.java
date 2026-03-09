package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_11_3Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;

    @BeforeEach
    void setUp() {
        values = new String[] { "val0", "val1", "val2" };
        mapping = new HashMap<>();
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 42L);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withValidMapping() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> returnedMap = csvRecord.putIn(map);
        assertSame(map, returnedMap);
        assertEquals(3, map.size());
        assertEquals("val0", map.get("key0"));
        assertEquals("val1", map.get("key1"));
        assertEquals("val2", map.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingIndexOutOfBounds() {
        mapping.put("keyOutOfBounds", values.length + 10);
        csvRecord = new CSVRecord(values, mapping, "comment", 42L);
        Map<String, String> map = new HashMap<>();
        Map<String, String> returnedMap = csvRecord.putIn(map);
        assertSame(map, returnedMap);
        // Should not contain keyOutOfBounds because index is out of values range
        assertFalse(map.containsKey("keyOutOfBounds"));
        // Other keys still present
        assertEquals("val0", map.get("key0"));
        assertEquals("val1", map.get("key1"));
        assertEquals("val2", map.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping() {
        CSVRecord record = new CSVRecord(values, new HashMap<>(), "comment", 42L);
        Map<String, String> map = new HashMap<>();
        Map<String, String> returnedMap = record.putIn(map);
        assertSame(map, returnedMap);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyValues() {
        CSVRecord record = new CSVRecord(new String[0], mapping, "comment", 42L);
        Map<String, String> map = new HashMap<>();
        Map<String, String> returnedMap = record.putIn(map);
        assertSame(map, returnedMap);
        // No values can be put because values array is empty
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMockedMap() {
        @SuppressWarnings("unchecked")
        Map<String, String> mockMap = mock(Map.class);
        Map<String, String> returnedMap = csvRecord.putIn(mockMap);
        assertSame(mockMap, returnedMap);
        for (Entry<String, Integer> entry : mapping.entrySet()) {
            int index = entry.getValue();
            if (index < values.length) {
                verify(mockMap).put(entry.getKey(), values[index]);
            }
        }
        verifyNoMoreInteractions(mockMap);
    }

    @Test
    @Timeout(8000)
    void testPutIn_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);
        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> returnedMap = (Map<String, String>) putInMethod.invoke(csvRecord, map);
        assertSame(map, returnedMap);
        assertEquals(3, map.size());
        assertEquals("val0", map.get("key0"));
        assertEquals("val1", map.get("key1"));
        assertEquals("val2", map.get("key2"));
    }
}