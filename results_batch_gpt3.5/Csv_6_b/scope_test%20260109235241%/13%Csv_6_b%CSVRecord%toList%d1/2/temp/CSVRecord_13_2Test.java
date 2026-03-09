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

class CSVRecord_13_2Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[] { "val1", "val2", "val3" };
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        comment = "test comment";
        recordNumber = 123L;

        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testToList_returnsListOfValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecord);

        assertNotNull(list);
        assertEquals(values.length, list.size());
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testToList_emptyValues_returnsEmptyList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, mapping, comment, recordNumber);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(emptyRecord);

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}