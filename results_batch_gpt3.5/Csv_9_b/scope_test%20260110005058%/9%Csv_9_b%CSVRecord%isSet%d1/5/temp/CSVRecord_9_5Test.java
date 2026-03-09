package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_5Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[] {"val0", "val1", "val2"};
        mapping = new HashMap<>();
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        // Using constructor: CSVRecord(final String[] values, final Map<String, Integer> mapping, final String comment, final long recordNumber)
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedAndIndexInBounds() {
        assertTrue(csvRecord.isSet("key0"));
        assertTrue(csvRecord.isSet("key1"));
        assertTrue(csvRecord.isSet("key2"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedButIndexOutOfBounds() throws Exception {
        // Add a mapping with index >= values.length
        mapping.put("keyOutOfBounds", values.length);
        // Recreate csvRecord with updated mapping
        csvRecord = new CSVRecord(values, mapping, null, 1L);
        assertFalse(csvRecord.isSet("keyOutOfBounds"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NotMapped() {
        assertFalse(csvRecord.isSet("notMappedKey"));
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyValues() {
        // Use a fresh mapping for this test
        Map<String, Integer> emptyValuesMapping = new HashMap<>();
        emptyValuesMapping.put("key0", 0);
        CSVRecord emptyValuesRecord = new CSVRecord(new String[0], emptyValuesMapping, null, 1L);
        // Even if mapped, index 0 is out of bounds for empty values
        assertFalse(emptyValuesRecord.isSet("key0"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyMapping() {
        CSVRecord emptyMappingRecord = new CSVRecord(values, Collections.emptyMap(), null, 1L);
        assertFalse(emptyMappingRecord.isSet("key0"));
    }
}