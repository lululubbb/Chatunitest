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

public class CSVRecord_7_3Test {

    @Test
    @Timeout(8000)
    public void testIsConsistentMappingNull() throws Exception {
        // mapping == null => isConsistent() returns true
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = null;
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean consistent = record.isConsistent();

        assertTrue(consistent, "Expected isConsistent() to return true when mapping is null");
    }

    @Test
    @Timeout(8000)
    public void testIsConsistentMappingSizeEqualsValuesLength() throws Exception {
        // mapping != null and mapping.size() == values.length => true
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "comment";
        long recordNumber = 2L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean consistent = record.isConsistent();

        assertTrue(consistent, "Expected isConsistent() to return true when mapping size equals values length");
    }

    @Test
    @Timeout(8000)
    public void testIsConsistentMappingSizeNotEqualsValuesLength() throws Exception {
        // mapping != null and mapping.size() != values.length => false
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "comment";
        long recordNumber = 3L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean consistent = record.isConsistent();

        assertFalse(consistent, "Expected isConsistent() to return false when mapping size does not equal values length");
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Class<?> clazz = Class.forName("org.apache.commons.csv.CSVRecord");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return (CSVRecord) constructor.newInstance(new Object[] {values, mapping, comment, recordNumber});
    }
}