package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_8_2Test {

    @Test
    @Timeout(8000)
    void testIsMapped_mappingNull() throws Exception {
        CSVRecord csvRecord = createCSVRecord(null);
        assertFalse(csvRecord.isMapped("anyKey"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingEmpty() throws Exception {
        Map<String, Integer> mapping = Collections.emptyMap();
        CSVRecord csvRecord = createCSVRecord(mapping);
        assertFalse(csvRecord.isMapped("anyKey"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingContainsKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        CSVRecord csvRecord = createCSVRecord(mapping);
        assertTrue(csvRecord.isMapped("key1"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingDoesNotContainKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        CSVRecord csvRecord = createCSVRecord(mapping);
        assertFalse(csvRecord.isMapped("key2"));
    }

    private CSVRecord createCSVRecord(Map<String, Integer> mapping) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(new String[]{"value1", "value2"}, mapping, null, 1L);
    }
}