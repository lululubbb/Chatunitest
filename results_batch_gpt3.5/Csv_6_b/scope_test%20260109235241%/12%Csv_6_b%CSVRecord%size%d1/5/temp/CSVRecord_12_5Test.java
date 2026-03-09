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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_12_5Test {

    private Class<?> csvRecordClass;
    private Constructor<?> csvRecordConstructor;

    @BeforeEach
    public void setUp() throws Exception {
        csvRecordClass = Class.forName("org.apache.commons.csv.CSVRecord");
        csvRecordConstructor = csvRecordClass.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        csvRecordConstructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testSize_WithNonEmptyValuesArray() throws Exception {
        String[] values = new String[] { "val1", "val2", "val3" };
        Map<String, Integer> mapping = new HashMap<>();
        Object csvRecord = csvRecordConstructor.newInstance(values, mapping, null, 1L);

        Method sizeMethod = csvRecordClass.getDeclaredMethod("size");
        sizeMethod.setAccessible(true);

        int size = (int) sizeMethod.invoke(csvRecord);
        assertEquals(3, size);
    }

    @Test
    @Timeout(8000)
    public void testSize_WithEmptyValuesArray() throws Exception {
        String[] values = new String[0];
        Map<String, Integer> mapping = Collections.emptyMap();
        Object csvRecord = csvRecordConstructor.newInstance(values, mapping, null, 2L);

        Method sizeMethod = csvRecordClass.getDeclaredMethod("size");
        sizeMethod.setAccessible(true);

        int size = (int) sizeMethod.invoke(csvRecord);
        assertEquals(0, size);
    }

    @Test
    @Timeout(8000)
    public void testSize_WithNullValuesField() throws Exception {
        // Create instance with non-null values
        String[] values = new String[] { "a" };
        Map<String, Integer> mapping = Collections.emptyMap();
        Object csvRecord = csvRecordConstructor.newInstance(values, mapping, null, 3L);

        // Use reflection to remove final modifier and set private final values field to null
        Field valuesField = csvRecordClass.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier for 'values' field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(csvRecord, null);

        Method sizeMethod = csvRecordClass.getDeclaredMethod("size");
        sizeMethod.setAccessible(true);

        // Expect NullPointerException because size() returns values.length and values is null
        try {
            sizeMethod.invoke(csvRecord);
        } catch (java.lang.reflect.InvocationTargetException e) {
            // InvocationTargetException wraps NullPointerException
            Throwable cause = e.getCause();
            if (!(cause instanceof NullPointerException)) {
                throw e;
            }
        }
    }
}