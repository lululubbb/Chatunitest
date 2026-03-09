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
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_7_6Test {

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingNull() throws Exception {
        CSVRecord record = createCSVRecord(new String[]{"a", "b"}, null);
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeEqualsValuesLength() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        String[] values = new String[]{"val1", "val2"};
        CSVRecord record = createCSVRecord(values, mapping);
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeNotEqualsValuesLength() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        String[] values = new String[]{"val1", "val2"};
        CSVRecord record = createCSVRecord(values, mapping);
        assertFalse(record.isConsistent());
    }
}