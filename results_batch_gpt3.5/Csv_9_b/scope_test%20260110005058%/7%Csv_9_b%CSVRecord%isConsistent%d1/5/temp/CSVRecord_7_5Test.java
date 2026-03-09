package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVRecord_7_5Test {

    @Test
    @Timeout(8000)
    void testIsConsistent_MappingNull() throws Exception {
        // Prepare values array
        String[] values = new String[] {"a", "b", "c"};

        // Use reflection to create CSVRecord instance with mapping = null
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) values, null, null, 1L);

        // mapping == null -> isConsistent() returns true
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_MappingSizeEqualsValuesLength() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) values, mapping, null, 2L);

        // mapping.size() == values.length -> isConsistent() returns true
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_MappingSizeNotEqualsValuesLength() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) values, mapping, null, 3L);

        // mapping.size() != values.length -> isConsistent() returns false
        assertFalse(record.isConsistent());
    }
}