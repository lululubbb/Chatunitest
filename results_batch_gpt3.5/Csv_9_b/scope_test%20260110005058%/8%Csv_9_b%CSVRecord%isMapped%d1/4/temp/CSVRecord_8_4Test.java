package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class CSVRecord_8_4Test {

    @Test
    @Timeout(8000)
    public void testIsMapped_withNullMapping() throws Exception {
        CSVRecord record = createCSVRecord(null);
        boolean result = record.isMapped("anyName");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withEmptyMapping() throws Exception {
        Map<String, Integer> mapping = Collections.emptyMap();
        CSVRecord record = createCSVRecord(mapping);
        boolean result = record.isMapped("anyName");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingContainingKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        CSVRecord record = createCSVRecord(mapping);
        boolean result = record.isMapped("key1");
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingNotContainingKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        CSVRecord record = createCSVRecord(mapping);
        boolean result = record.isMapped("key2");
        assertFalse(result);
    }

    private CSVRecord createCSVRecord(Map<String, Integer> mapping) throws Exception {
        String[] values = new String[] {"value1"};
        String comment = null;
        long recordNumber = 1L;

        Constructor<CSVRecord> ctor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);
        return ctor.newInstance(values, mapping, comment, recordNumber);
    }
}