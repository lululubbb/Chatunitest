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

public class CSVRecord_8_3Test {

    private CSVRecord csvRecord;

    @BeforeEach
    void setUp() throws Exception {
        // Prepare dummy values array
        String[] values = new String[] {"a", "b", "c"};
        // Prepare dummy mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        mapping.put("key2", 1);
        // Create instance via constructor
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingContainsKey_true() {
        assertTrue(csvRecord.isMapped("key1"));
        assertTrue(csvRecord.isMapped("key2"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingDoesNotContainKey_false() {
        assertFalse(csvRecord.isMapped("unknown"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingNull_false() throws Exception {
        // Use reflection to set private final mapping field to null
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(mappingField, mappingField.getModifiers() & ~Modifier.FINAL);

        mappingField.set(csvRecord, null);

        assertFalse(csvRecord.isMapped("key1"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_nullKey_false() {
        // mapping contains keys, but null key should return false since containsKey(null) is false
        assertFalse(csvRecord.isMapped(null));
    }
}