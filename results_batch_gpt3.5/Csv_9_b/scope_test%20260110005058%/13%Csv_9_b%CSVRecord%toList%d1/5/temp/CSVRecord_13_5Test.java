package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_13_5Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[] {"value1", "value2", "value3"};
        mapping = mock(Map.class);
        comment = "comment";
        recordNumber = 42L;

        // Use reflection to access the package-private constructor
        csvRecord = createCSVRecord(values, mapping, comment, recordNumber);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testToList_returnsListOfValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(csvRecord);

        assertNotNull(result);
        assertEquals(values.length, result.size());
        assertEquals(Arrays.asList(values), result);
    }

    @Test
    @Timeout(8000)
    public void testToList_emptyValues_returnsEmptyList() throws Exception {
        CSVRecord emptyRecord = createCSVRecord(new String[0], mapping, comment, recordNumber);
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(emptyRecord);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}