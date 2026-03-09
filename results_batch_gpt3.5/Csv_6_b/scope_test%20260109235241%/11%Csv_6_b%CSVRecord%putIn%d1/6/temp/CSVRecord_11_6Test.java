package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_11_6Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        mapping.put("header3", 2);

        values = new String[] { "value1", "value2", "value3" };

        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMap() {
        Map<String, String> map = new HashMap<>();

        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNonEmptyMapOverwrites() {
        Map<String, String> map = new HashMap<>();
        map.put("header1", "oldValue");
        map.put("otherKey", "otherValue");

        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertEquals(4, map.size());
        assertEquals("value1", map.get("header1")); // overwritten
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
        assertEquals("otherValue", map.get("otherKey")); // untouched
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to set private final field mapping to empty map
        Map<String, Integer> emptyMapping = new HashMap<>();
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(mappingField, mappingField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        mappingField.set(csvRecord, emptyMapping);

        Map<String, String> map = new HashMap<>();
        map.put("existingKey", "existingValue");

        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result);
        assertEquals(1, map.size());
        assertEquals("existingValue", map.get("existingKey"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingIndexOutOfBounds() {
        // Add a mapping entry with index out of bounds to test behavior
        mapping.put("badHeader", 10);

        // Recreate CSVRecord with this mapping
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> map = new HashMap<>();

        // The method putIn does not have explicit bounds check, so it will throw ArrayIndexOutOfBoundsException
        // We test that this exception is thrown
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.putIn(map));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withDifferentMapImplementation() {
        // Use LinkedHashMap to test generic M
        java.util.LinkedHashMap<String, String> linkedHashMap = new java.util.LinkedHashMap<>();
        linkedHashMap.put("existingKey", "existingValue");

        java.util.LinkedHashMap<String, String> result = csvRecord.putIn(linkedHashMap);

        assertSame(linkedHashMap, result);
        assertEquals(4, linkedHashMap.size());
        assertEquals("value1", linkedHashMap.get("header1"));
        assertEquals("value2", linkedHashMap.get("header2"));
        assertEquals("value3", linkedHashMap.get("header3"));
        assertEquals("existingValue", linkedHashMap.get("existingKey"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_viaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertSame(map, result);
        assertEquals(3, map.size());
        assertEquals("value1", map.get("header1"));
        assertEquals("value2", map.get("header2"));
        assertEquals("value3", map.get("header3"));
    }
}