package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_15_6Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws Exception {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testToString_withValues() throws Exception {
        String[] values = new String[] { "a", "b", "c" };
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        String expected = "[a, b, c]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testToString_withEmptyValues() throws Exception {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        String expected = "[]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testToString_withNullValuesField() throws Exception {
        // Create instance with normal values first
        String[] values = new String[] { "x", "y" };
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        // Use reflection to set private final values field to null
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(record, null);

        // toString should handle null gracefully (Arrays.toString(null) returns "null")
        String expected = "null";
        String actual = record.toString();

        assertEquals(expected, actual);
    }
}