package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_14_4Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testToMap_withValuesAndMapping() throws Exception {
        String[] values = new String[] { "val0", "val1", "val2" };
        csvRecord = createCSVRecord(values, mapping, null, 1L);

        Map<String, String> map = csvRecord.toMap();

        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("val0", map.get("A"));
        assertEquals("val1", map.get("B"));
        assertEquals("val2", map.get("C"));
    }

    @Test
    @Timeout(8000)
    void testToMap_withEmptyValues() throws Exception {
        String[] values = new String[0];
        csvRecord = createCSVRecord(values, mapping, null, 1L);

        Map<String, String> map = csvRecord.toMap();

        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testToMap_withNullMapping() throws Exception {
        String[] values = new String[] { "val0" };
        csvRecord = createCSVRecord(values, null, null, 1L);

        Map<String, String> map = csvRecord.toMap();

        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_directlyUsingReflection() throws Exception {
        String[] values = new String[] { "v1", "v2" };
        csvRecord = createCSVRecord(values, mapping, null, 1L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertSame(map, result);
        assertEquals(2, result.size());
        assertEquals("v1", result.get("A"));
        assertEquals("v2", result.get("B"));
    }
}