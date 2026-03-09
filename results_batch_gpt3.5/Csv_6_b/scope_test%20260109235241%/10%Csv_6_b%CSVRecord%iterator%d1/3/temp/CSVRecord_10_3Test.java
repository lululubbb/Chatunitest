package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_10_3Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "comment";
        recordNumber = 123L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIterator_returnsIteratorOverValuesList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);

        // Use reflection to invoke private toList() method to get expected list
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> expectedList = (List<String>) toListMethod.invoke(csvRecord);

        // Check iterator iterates over all elements in expectedList in order
        for (String expectedValue : expectedList) {
            assertTrue(iterator.hasNext());
            String actualValue = iterator.next();
            assertEquals(expectedValue, actualValue);
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testIterator_emptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVRecord emptyRecord = new CSVRecord(new String[0], new HashMap<>(), null, 0L);
        Iterator<String> iterator = emptyRecord.iterator();
        assertNotNull(iterator);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> expectedList = (List<String>) toListMethod.invoke(emptyRecord);

        assertTrue(expectedList.isEmpty());
        assertFalse(iterator.hasNext());
    }
}