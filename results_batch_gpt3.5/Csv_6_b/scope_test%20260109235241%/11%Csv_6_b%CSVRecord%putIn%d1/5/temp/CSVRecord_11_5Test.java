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

class CSVRecord_11_5Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("key1", 0);
        mapping.put("key2", 1);
        values = new String[] {"value1", "value2"};
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withPopulatedMapping() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);

        assertSame(map, result, "Returned map should be the same instance as the input map");
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
        assertEquals(2, map.size());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping() throws Exception {
        Map<String, Integer> emptyMapping = new HashMap<>();
        String[] vals = new String[] {"v1", "v2"};
        CSVRecord emptyMappingRecord = new CSVRecord(vals, emptyMapping, null, 0L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> map = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(emptyMappingRecord, map);

        assertSame(map, result);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNullValuesInArray() {
        mapping = new HashMap<>();
        mapping.put("key1", 0);
        mapping.put("key2", 1);
        mapping.put("key3", 2);
        String[] valsWithNull = new String[] {"v1", "v2", null};
        CSVRecord recordWithNull = new CSVRecord(valsWithNull, mapping, null, 0L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = recordWithNull.putIn(map);

        assertSame(map, result);
        assertEquals("v1", map.get("key1"));
        assertEquals("v2", map.get("key2"));
        assertNull(map.get("key3"));
        assertEquals(3, map.size());
    }
}