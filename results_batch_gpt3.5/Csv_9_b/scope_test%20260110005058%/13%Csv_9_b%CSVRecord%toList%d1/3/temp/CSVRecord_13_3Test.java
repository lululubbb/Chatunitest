package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_13_3Test {

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
        comment = "Test comment";
        recordNumber = 123L;

        csvRecord = createCSVRecord(values, mapping, comment, recordNumber);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) {
        try {
            Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[] {values, mapping, comment, recordNumber});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testToList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(csvRecord);

        assertNotNull(result);
        assertEquals(values.length, result.size());
        assertEquals(Arrays.asList(values), result);

        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], result.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testToListWithEmptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVRecord emptyRecord = createCSVRecord(new String[0], new HashMap<>(), null, 0L);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(emptyRecord);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}