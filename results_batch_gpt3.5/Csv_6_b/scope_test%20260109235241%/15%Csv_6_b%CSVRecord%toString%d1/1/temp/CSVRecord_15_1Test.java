package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class CSVRecord_15_1Test {

    @Test
    @Timeout(8000)
    public void testToString_withValues() throws Exception {
        // Prepare values array
        String[] values = new String[] {"value1", "value2", "value3"};
        // Prepare mapping (can be empty, not used in toString)
        Map<String, Integer> mapping = new HashMap<>();
        // comment and recordNumber can be any values
        String comment = "comment";
        long recordNumber = 123L;

        // Use constructor via reflection since it's package-private
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object)values, mapping, comment, recordNumber);

        String expected = "[value1, value2, value3]";
        assertEquals(expected, record.toString());
    }

    @Test
    @Timeout(8000)
    public void testToString_withEmptyValues() throws Exception {
        // Prepare empty values array
        String[] values = new String[0];
        Map<String, Integer> mapping = Collections.emptyMap();
        String comment = null;
        long recordNumber = 0L;

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object)values, mapping, comment, recordNumber);

        String expected = "[]";
        assertEquals(expected, record.toString());
    }

    @Test
    @Timeout(8000)
    public void testToString_withNullValuesField() throws Exception {
        // Create instance with valid values first
        String[] values = new String[] {"a", "b"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "";
        long recordNumber = 1L;

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object)values, mapping, comment, recordNumber);

        // Use reflection to set private final field 'values' to null to test behavior
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier from the field 'values' to allow setting it to null
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(record, null);

        // toString should throw NullPointerException as Arrays.toString(null) throws NPE
        assertThrows(NullPointerException.class, record::toString);
    }
}