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
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

public class CSVRecord_7_1Test {

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingNull() throws Exception {
        // Create CSVRecord with mapping = null, values non-null
        CSVRecord record = createCSVRecord(new String[]{"a", "b", "c"}, null, "comment", 1L);
        boolean result = invokeIsConsistent(record);
        assertTrue(result, "Expected true when mapping is null");
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeEqualsValuesLength() throws Exception {
        // mapping size == values.length
        String[] values = new String[]{"val1", "val2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = createCSVRecord(values, mapping, "comment", 1L);
        boolean result = invokeIsConsistent(record);
        assertTrue(result, "Expected true when mapping size equals values length");
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_mappingSizeNotEqualsValuesLength() throws Exception {
        // mapping size != values.length
        String[] values = new String[]{"val1", "val2", "val3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = createCSVRecord(values, mapping, "comment", 1L);
        boolean result = invokeIsConsistent(record);
        assertFalse(result, "Expected false when mapping size does not equal values length");
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber)
            throws Exception {
        Constructor<CSVRecord> ctor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);
        return ctor.newInstance(values, mapping, comment, recordNumber);
    }

    private boolean invokeIsConsistent(CSVRecord record)
            throws Exception {
        Method method = CSVRecord.class.getDeclaredMethod("isConsistent");
        method.setAccessible(true);
        return (boolean) method.invoke(record);
    }
}