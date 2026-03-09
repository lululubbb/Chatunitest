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

public class CSVRecord_7_4Test {

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingNull_returnsTrue() throws Exception {
        // Create CSVRecord instance with mapping = null
        String[] values = new String[] { "a", "b", "c" };
        Map<String, Integer> mapping = null;
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        // isConsistent is public, call directly
        boolean result = record.isConsistent();

        assertTrue(result, "Expected isConsistent to return true when mapping is null");
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeEqualsValuesLength_returnsTrue() throws Exception {
        // mapping size == values.length
        String[] values = new String[] { "a", "b", "c" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean result = record.isConsistent();

        assertTrue(result, "Expected isConsistent to return true when mapping size equals values length");
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeNotEqualsValuesLength_returnsFalse() throws Exception {
        // mapping size != values.length
        String[] values = new String[] { "a", "b", "c", "d" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean result = record.isConsistent();

        assertFalse(result, "Expected isConsistent to return false when mapping size does not equal values length");
    }

    @SuppressWarnings("unchecked")
    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        // CSVRecord constructor is package-private, so instantiate via reflection
        Class<?> clazz = Class.forName("org.apache.commons.csv.CSVRecord");
        Constructor<?> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return (CSVRecord) constructor.newInstance(values, mapping, comment, recordNumber);
    }
}