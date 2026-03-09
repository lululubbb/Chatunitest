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
    void setUp() throws Exception {
        values = new String[] { "v0", "v1", "v2" };
        mapping = new HashMap<>();
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);

        // Use reflection to call package-private constructor
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
    }

    @SuppressWarnings("unchecked")
    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        // Constructor is package-private, so use reflection
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testIsSet_MappedAndIndexInRange_ReturnsTrue() {
        assertTrue(csvRecord.isSet("key0"));
        assertTrue(csvRecord.isSet("key1"));
        assertTrue(csvRecord.isSet("key2"));
    }

    @Test
    @Timeout(8000)
    void testIsSet_MappedButIndexOutOfRange_ReturnsFalse() throws Exception {
        // Add mapping with index equal to values.length (out of range)
        mapping.put("keyOutOfRange", values.length);
        // Recreate CSVRecord with updated mapping
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);

        assertFalse(csvRecord.isSet("keyOutOfRange"));
    }

    @Test
    @Timeout(8000)
    void testIsSet_NotMapped_ReturnsFalse() {
        assertFalse(csvRecord.isSet("nonexistentKey"));
    }

    @Test
    @Timeout(8000)
    void testIsSet_NullName_ReturnsFalse() {
        assertFalse(csvRecord.isSet(null));
    }
}