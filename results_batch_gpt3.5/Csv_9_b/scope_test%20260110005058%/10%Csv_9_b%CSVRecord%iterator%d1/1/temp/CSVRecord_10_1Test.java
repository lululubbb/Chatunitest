package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_10_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[] { "value1", "value2", "value3" };
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
    public void testIterator_returnsIteratorOverValues() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);
        List<String> iteratedValues = new ArrayList<>();
        while (iterator.hasNext()) {
            iteratedValues.add(iterator.next());
        }
        assertEquals(Arrays.asList(values), iteratedValues);
    }

    @Test
    @Timeout(8000)
    public void testIterator_emptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create CSVRecord with empty values and empty mapping
        CSVRecord emptyRecord = new CSVRecord(new String[0], Collections.emptyMap(), null, 0L);
        Iterator<String> iterator = emptyRecord.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());

        // Also test private toList method returns empty list for empty values
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(emptyRecord);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testToList_reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecord);
        assertNotNull(list);
        assertEquals(values.length, list.size());
        assertEquals(Arrays.asList(values), list);
    }
}