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

public class CSVRecord_7_5Test {

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingNull() throws Exception {
        // Create CSVRecord instance with mapping = null
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        String[] values = new String[] {"a", "b", "c"};
        CSVRecord record = constructor.newInstance((Object) values, null, null, 1L);

        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeEqualsValuesLength() throws Exception {
        // mapping size equals values length -> should return true
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        mapping.put("three", 2);
        CSVRecord record = constructor.newInstance((Object) values, mapping, "comment", 1L);

        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeNotEqualsValuesLength() throws Exception {
        // mapping size different from values length -> should return false
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        // mapping size = 2, values length = 3
        CSVRecord record = constructor.newInstance((Object) values, mapping, "comment", 1L);

        assertFalse(record.isConsistent());
    }
}