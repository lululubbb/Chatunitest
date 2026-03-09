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

public class CSVRecord_9_6Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        values = new String[] {"val0", "val1", "val2"};
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        Map<String, Integer> mappingCopy = new HashMap<>(mapping);
        String[] valuesCopy = values.clone();
        return constructor.newInstance(valuesCopy, mappingCopy, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameNotMapped() {
        String name = "notMapped";
        assertFalse(csvRecord.isSet(name));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexInRange() {
        String name = "field1";
        mapping.put(name, 1);
        csvRecord = recreateCSVRecordWithMapping(mapping, values);
        assertTrue(csvRecord.isSet(name));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexEqualsLength() {
        String name = "fieldOutOfRange";
        mapping.put(name, values.length);
        csvRecord = recreateCSVRecordWithMapping(mapping, values);
        assertFalse(csvRecord.isSet(name));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexGreaterThanLength() {
        String name = "fieldGreaterOutOfRange";
        mapping.put(name, values.length + 1);
        csvRecord = recreateCSVRecordWithMapping(mapping, values);
        assertFalse(csvRecord.isSet(name));
    }

    private CSVRecord recreateCSVRecordWithMapping(Map<String, Integer> mapping, String[] values) {
        try {
            return createCSVRecord(values, mapping, "comment", 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}