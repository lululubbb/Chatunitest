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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMap_returnsSameEmptyMap() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);
        assertSame(map, result);
        assertEquals(3, map.size());
        assertEquals("val0", map.get("key0"));
        assertEquals("val1", map.get("key1"));
        assertEquals("val2", map.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withPrePopulatedMap_overwritesAndAddsEntries() {
        Map<String, String> map = new HashMap<>();
        map.put("key0", "oldVal0");
        map.put("otherKey", "otherVal");

        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertEquals(4, map.size());
        assertEquals("val0", map.get("key0")); // overwritten
        assertEquals("val1", map.get("key1")); // added
        assertEquals("val2", map.get("key2")); // added
        assertEquals("otherVal", map.get("otherKey")); // untouched
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping_putsNothing() throws Exception {
        // Create CSVRecord with empty mapping
        CSVRecord emptyMappingRecord = new CSVRecord(values, Collections.emptyMap(), null, 0L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = emptyMappingRecord.putIn(map);

        assertSame(map, result);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNullValuesIndex_throwsArrayIndexOutOfBoundsException() {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("key0", 10); // index out of bounds

        CSVRecord badRecord = new CSVRecord(values, badMapping, null, 0L);

        Map<String, String> map = new HashMap<>();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> badRecord.putIn(map));
    }

    @Test
    @Timeout(8000)
    void testPutIn_reflectionInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertSame(map, result);
        assertEquals(3, map.size());
        assertEquals("val0", map.get("key0"));
        assertEquals("val1", map.get("key1"));
        assertEquals("val2", map.get("key2"));
    }
}