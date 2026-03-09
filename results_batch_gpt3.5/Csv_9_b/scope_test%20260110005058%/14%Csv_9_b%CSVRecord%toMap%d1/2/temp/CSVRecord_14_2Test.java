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

class CSVRecord_14_2Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        values = new String[] { "valueA", "valueB", "valueC" };
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testToMap_allMappedKeys() {
        Map<String, String> result = csvRecord.toMap();
        assertEquals(3, result.size());
        assertEquals("valueA", result.get("A"));
        assertEquals("valueB", result.get("B"));
        assertEquals("valueC", result.get("C"));
    }

    @Test
    @Timeout(8000)
    void testToMap_emptyValues() {
        CSVRecord emptyRecord = new CSVRecord(new String[0], new HashMap<>(), null, 1L);
        Map<String, String> result = emptyRecord.toMap();
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testToMap_nullValuesInArray() {
        String[] valsWithNull = new String[] { "val1", null, "val3" };
        Map<String, Integer> map = new HashMap<>();
        map.put("X", 0);
        map.put("Y", 1);
        map.put("Z", 2);
        CSVRecord recordWithNull = new CSVRecord(valsWithNull, map, null, 1L);
        Map<String, String> result = recordWithNull.toMap();
        assertEquals(3, result.size());
        assertEquals("val1", result.get("X"));
        assertNull(result.get("Y"));
        assertEquals("val3", result.get("Z"));
    }

    @Test
    @Timeout(8000)
    void testPutInViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertEquals(3, result.size());
        assertEquals("valueA", result.get("A"));
        assertEquals("valueB", result.get("B"));
        assertEquals("valueC", result.get("C"));
    }
}