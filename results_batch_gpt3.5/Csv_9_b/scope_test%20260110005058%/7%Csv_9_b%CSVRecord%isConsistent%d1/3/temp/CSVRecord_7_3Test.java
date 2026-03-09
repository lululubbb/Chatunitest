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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_7_3Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;

    @BeforeEach
    void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingNull() {
        // Create CSVRecord with mapping = null
        csvRecord = createCSVRecord(values, null, "comment", 1L);

        // isConsistent should return true when mapping is null
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeEqualsValuesLength() {
        // mapping size equals values length
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);

        // isConsistent should return true
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_mappingSizeNotEqualsValuesLength() {
        // mapping size not equal to values length
        Map<String, Integer> smallerMapping = new HashMap<>();
        smallerMapping.put("col1", 0);
        smallerMapping.put("col2", 1);
        // values length = 3, mapping size = 2

        csvRecord = createCSVRecord(values, smallerMapping, "comment", 1L);

        // isConsistent should return false
        assertFalse(csvRecord.isConsistent());
    }

    @SuppressWarnings("unchecked")
    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) {
        try {
            Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
            constructor.setAccessible(true);
            return constructor.newInstance((Object) values, mapping, comment, recordNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}