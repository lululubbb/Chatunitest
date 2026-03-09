package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

class CSVRecord_16_1Test {

    @Test
    @Timeout(8000)
    void testValuesReturnsCorrectArray() throws Exception {
        String[] expectedValues = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        mapping.put("three", 2);
        String comment = "comment";
        long recordNumber = 42L;

        // Create instance via constructor
        CSVRecord record = new CSVRecord(expectedValues, mapping, comment, recordNumber);

        // Access values() method directly since it is package-private
        String[] actualValues = record.values();

        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    @Timeout(8000)
    void testValuesWhenEmptyArray() throws Exception {
        String[] expectedValues = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 0L;

        CSVRecord record = new CSVRecord(expectedValues, mapping, comment, recordNumber);

        String[] actualValues = record.values();

        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    @Timeout(8000)
    void testValuesReflectiveAccess() throws Exception {
        String[] expectedValues = new String[] {"x", "y"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "c";
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(new String[0], mapping, comment, recordNumber);

        // Use reflection to set private final field 'values'
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier on the field (works on JDK 8; on later JDKs this might not work)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(record, expectedValues);

        // Invoke values() method and verify
        String[] actualValues = record.values();

        assertArrayEquals(expectedValues, actualValues);
    }
}