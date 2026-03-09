package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_13_4Test {

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
        comment = "test comment";
        recordNumber = 5L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testToListReturnsCorrectList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(csvRecord);

        assertNotNull(result);
        assertEquals(Arrays.asList(values), result);
        assertEquals(values.length, result.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], result.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testToListReturnsEmptyListWhenValuesEmpty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVRecord emptyCsvRecord = new CSVRecord(new String[0], mapping, comment, recordNumber);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(emptyCsvRecord);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}