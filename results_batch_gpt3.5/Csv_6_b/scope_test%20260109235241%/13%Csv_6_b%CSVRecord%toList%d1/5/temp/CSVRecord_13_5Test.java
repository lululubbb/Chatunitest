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

class CSVRecord_13_5Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[] { "value1", "value2", "value3" };
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "This is a comment";
        recordNumber = 123L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testToList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(csvRecord);

        assertNotNull(result);
        assertEquals(values.length, result.size());
        assertEquals(Arrays.asList(values), result);
        // Ensure the list is fixed-size (Arrays.asList returns fixed-size list)
        assertThrows(UnsupportedOperationException.class, () -> result.add("newValue"));
    }

    @Test
    @Timeout(8000)
    void testToListEmptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] emptyValues = new String[0];
        Map<String, Integer> emptyMapping = new HashMap<>();
        CSVRecord emptyRecord = new CSVRecord(emptyValues, emptyMapping, null, 0L);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(emptyRecord);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Ensure the list is fixed-size (Arrays.asList returns fixed-size list)
        assertThrows(UnsupportedOperationException.class, () -> result.add("newValue"));
    }
}