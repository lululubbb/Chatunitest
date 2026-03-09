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

class CSVRecord_11_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        values = new String[] { "value0", "value1", "value2" };
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMap_returnsEmptyMap() {
        csvRecord = new CSVRecord(values, new HashMap<>(), null, 1L);
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);
        assertTrue(result.isEmpty());
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withValidMapping_putsValuesInMap() {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);

        assertEquals(3, result.size());
        assertEquals("value0", result.get("key0"));
        assertEquals("value1", result.get("key1"));
        assertEquals("value2", result.get("key2"));
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingIndexOutOfBounds_skipsThatEntry() {
        mapping.put("key0", 0);
        mapping.put("keyOutOfBounds", 5); // index 5 does not exist in values
        csvRecord = new CSVRecord(values, mapping, null, 0L);

        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);

        assertEquals(1, result.size());
        assertEquals("value0", result.get("key0"));
        assertFalse(result.containsKey("keyOutOfBounds"));
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyValuesAndMapping() {
        csvRecord = new CSVRecord(new String[0], new HashMap<>(), null, 0L);
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);
        assertTrue(result.isEmpty());
        assertSame(map, result);
    }

    @Test
    @Timeout(8000)
    void testPutIn_privateMethodInvocation_reflection() throws Exception {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(values, mapping, null, 0L);
        Map<String, String> map = new HashMap<>();

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, map);

        assertEquals(1, result.size());
        assertEquals("value0", result.get("key0"));
        assertSame(map, result);
    }
}