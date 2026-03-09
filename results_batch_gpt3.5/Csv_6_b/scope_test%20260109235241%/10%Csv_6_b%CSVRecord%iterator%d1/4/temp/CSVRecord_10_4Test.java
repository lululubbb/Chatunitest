package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_10_4Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[] { "value1", "value2", "value3" };
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "This is a comment";
        recordNumber = 42L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIterator_iteratesOverValues() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);

        int count = 0;
        while (iterator.hasNext()) {
            String val = iterator.next();
            assertEquals(values[count], val);
            count++;
        }
        assertEquals(values.length, count);
    }

    @Test
    @Timeout(8000)
    public void testIterator_emptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create CSVRecord with empty values
        CSVRecord emptyRecord = new CSVRecord(new String[0], new HashMap<>(), comment, recordNumber);
        Iterator<String> iterator = emptyRecord.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());

        // Also test private toList returns empty list for empty values
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(emptyRecord);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testToList_privateMethod_returnsCorrectList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecord);
        assertNotNull(list);
        assertEquals(values.length, list.size());
        assertIterableEquals(Arrays.asList(values), list);
    }
}