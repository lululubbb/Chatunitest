package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class CSVRecord_16_3Test {

    private CSVRecord csvRecord;
    private String[] valuesArray;
    private Map<String, Integer> mapping;

    @BeforeEach
    void setUp() {
        valuesArray = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        csvRecord = new CSVRecord(valuesArray, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testValuesReturnsCorrectArray() {
        String[] returnedValues = csvRecord.values();
        assertNotNull(returnedValues);
        assertArrayEquals(valuesArray, returnedValues);
    }

    @Test
    @Timeout(8000)
    void testValuesReturnsEmptyArrayWhenValuesFieldIsEmpty() throws Exception {
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(csvRecord, new String[0]);

        String[] returnedValues = csvRecord.values();
        assertNotNull(returnedValues);
        assertEquals(0, returnedValues.length);
    }

    @Test
    @Timeout(8000)
    void testValuesUsingReflection() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        Object result = valuesMethod.invoke(csvRecord);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        assertArrayEquals(valuesArray, (String[]) result);
    }

    @Test
    @Timeout(8000)
    void testValuesWithNullValuesField() throws Exception {
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        valuesField.set(csvRecord, null);

        String[] returnedValues = csvRecord.values();
        assertNull(returnedValues);
    }
}