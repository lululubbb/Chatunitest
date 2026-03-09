package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_8_5Test {

    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare default values for constructor
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        mapping.put("key2", 1);
        String comment = "comment";
        long recordNumber = 1L;

        // Use constructor to instantiate CSVRecord
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_MappingNotNullAndContainsKey() {
        assertTrue(csvRecord.isMapped("key1"), "Expected isMapped to return true for existing key");
        assertTrue(csvRecord.isMapped("key2"), "Expected isMapped to return true for existing key");
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_MappingNotNullButDoesNotContainKey() {
        assertFalse(csvRecord.isMapped("nonexistent"), "Expected isMapped to return false for non-existing key");
        assertFalse(csvRecord.isMapped(null), "Expected isMapped to return false for null key");
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_MappingIsNull() throws Exception {
        // Use reflection to set private final field mapping to null
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(mappingField, mappingField.getModifiers() & ~Modifier.FINAL);

        mappingField.set(csvRecord, null);

        assertFalse(csvRecord.isMapped("key1"), "Expected isMapped to return false when mapping is null");
        assertFalse(csvRecord.isMapped(null), "Expected isMapped to return false when mapping is null and key is null");
    }
}