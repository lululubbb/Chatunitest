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

public class CSVRecord_7_1Test {

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> ctor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);
        // Defensive copy of values array to avoid potential issues
        String[] valuesCopy = values != null ? values.clone() : null;
        return ctor.newInstance(valuesCopy, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingNull() throws Exception {
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = null;
        CSVRecord csvRecord = createCSVRecord(values, mapping, null, 1L);

        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeEqualsValuesLength() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord csvRecord = createCSVRecord(values, mapping, "comment", 2L);

        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeNotEqualsValuesLength() throws Exception {
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord csvRecord = createCSVRecord(values, mapping, "comment", 3L);

        assertFalse(csvRecord.isConsistent());
    }
}