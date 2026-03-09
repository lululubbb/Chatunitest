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

public class CSVRecord_10_6Test {

    private CSVRecord csvRecordWithValues;
    private CSVRecord csvRecordEmpty;

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[] {"a", "b", "c"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "comment";
        recordNumber = 1L;

        // Use reflection to get the constructor since CSVRecord constructor is package-private
        try {
            java.lang.reflect.Constructor<CSVRecord> constructor =
                CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
            constructor.setAccessible(true);
            csvRecordWithValues = constructor.newInstance(values, mapping, comment, recordNumber);
            csvRecordEmpty = constructor.newInstance(new String[0], new HashMap<>(), null, 0L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testIterator_withValues() {
        Iterator<String> iterator = csvRecordWithValues.iterator();
        assertNotNull(iterator);
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testIterator_empty() {
        Iterator<String> iterator = csvRecordEmpty.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testToListUsingReflection_withValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecordWithValues);
        assertNotNull(list);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testToListUsingReflection_empty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecordEmpty);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}