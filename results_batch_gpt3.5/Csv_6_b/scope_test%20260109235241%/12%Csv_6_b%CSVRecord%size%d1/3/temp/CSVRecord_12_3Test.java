package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_12_3Test {

    private CSVRecord csvRecordWithValues;
    private CSVRecord csvRecordEmpty;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare values array
        String[] values = new String[] { "val1", "val2", "val3" };
        // Prepare mapping (can be empty for this test)
        java.util.Map<String, Integer> mapping = new java.util.HashMap<>();

        // Use reflection to get the package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, java.util.Map.class, String.class, long.class);
        constructor.setAccessible(true);

        // Instance with values
        csvRecordWithValues = constructor.newInstance(values, mapping, null, 1L);

        // Instance with empty values array
        String[] emptyValues = new String[0];
        csvRecordEmpty = constructor.newInstance(emptyValues, mapping, null, 2L);
    }

    @Test
    @Timeout(8000)
    public void testSize_withValues() {
        int size = csvRecordWithValues.size();
        assertEquals(3, size);
    }

    @Test
    @Timeout(8000)
    public void testSize_emptyValues() {
        int size = csvRecordEmpty.size();
        assertEquals(0, size);
    }
}