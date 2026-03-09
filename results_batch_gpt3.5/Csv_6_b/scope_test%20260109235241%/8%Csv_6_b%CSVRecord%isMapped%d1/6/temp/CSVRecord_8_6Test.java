package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_8_6Test {

    private CSVRecord csvRecordWithMapping;
    private CSVRecord csvRecordWithoutMapping;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare mapping map
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        // Use reflection to access package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        // Create CSVRecord instance with mapping
        csvRecordWithMapping = constructor.newInstance(new Object[]{new String[]{"val1", "val2"}, mapping, "comment", 1L});

        // Create CSVRecord instance without mapping (null)
        csvRecordWithoutMapping = constructor.newInstance(new Object[]{new String[]{"val1", "val2"}, null, "comment", 1L});
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingContainingKey() {
        assertTrue(csvRecordWithMapping.isMapped("header1"));
        assertTrue(csvRecordWithMapping.isMapped("header2"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingNotContainingKey() {
        assertFalse(csvRecordWithMapping.isMapped("header3"));
        assertFalse(csvRecordWithMapping.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withNullMapping() {
        assertFalse(csvRecordWithoutMapping.isMapped("header1"));
        assertFalse(csvRecordWithoutMapping.isMapped(null));
    }
}