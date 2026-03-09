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

public class CSVRecord_7_2Test {

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingNull() throws Exception {
        // Prepare CSVRecord instance with mapping = null using reflection
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = null;
        String comment = "comment";
        long recordNumber = 123L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        // Invoke isConsistent() directly
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeEqualsValuesLength() throws Exception {
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        String comment = "comment";
        long recordNumber = 123L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeNotEqualsValuesLength() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        String comment = "comment";
        long recordNumber = 123L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        assertFalse(record.isConsistent());
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> ctor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);
        return ctor.newInstance(values, mapping, comment, recordNumber);
    }
}