package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVRecord_13_6Test {

    @Test
    @Timeout(8000)
    void testToListReturnsValuesAsList() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Prepare data
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "comment";
        long recordNumber = 1L;

        // Create CSVRecord instance
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // Access private method toList via reflection
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(record);

        // Verify returned list contents and type
        assertNotNull(result);
        assertEquals(values.length, result.size());
        assertEquals(Arrays.asList(values), result);

        // Check that modifying the returned list throws UnsupportedOperationException (Arrays.asList returns fixed-size list)
        assertThrows(UnsupportedOperationException.class, () -> result.add("d"));
    }

    @Test
    @Timeout(8000)
    void testToListWithEmptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Prepare data with empty values array
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 0L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(record);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}