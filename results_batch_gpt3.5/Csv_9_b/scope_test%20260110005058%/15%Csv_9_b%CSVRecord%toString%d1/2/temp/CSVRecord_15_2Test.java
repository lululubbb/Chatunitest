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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_15_2Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, SecurityException {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testToStringWithValues() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] values = new String[] { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = constructor.newInstance((Object) values, mapping, null, 1L);

        String expected = "[value1, value2, value3]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testToStringWithEmptyValues() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = constructor.newInstance((Object) values, mapping, null, 0L);

        String expected = "[]";
        String actual = record.toString();

        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testToStringWithNullValuesArray() throws Exception {
        CSVRecord record = constructor.newInstance((Object) new String[0], new HashMap<>(), null, 0L);

        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier on the field 'values'
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(record, null);

        String expected = "null";
        String actual = record.toString();

        assertEquals(expected, actual);
    }
}