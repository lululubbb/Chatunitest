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

class CSVRecord_7_2Test {

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingNull() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = null;
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean result = record.isConsistent();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeEqualsValuesLength() throws Exception {
        String[] values = new String[] {"val1", "val2", "val3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = null;
        long recordNumber = 10L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean result = record.isConsistent();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeNotEqualsValuesLength() throws Exception {
        String[] values = new String[] {"val1", "val2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = null;
        long recordNumber = 5L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        boolean result = record.isConsistent();
        assertFalse(result);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }
}