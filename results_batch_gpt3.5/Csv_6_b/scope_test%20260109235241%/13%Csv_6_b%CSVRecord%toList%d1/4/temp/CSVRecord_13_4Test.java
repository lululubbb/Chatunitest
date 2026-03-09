package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class CSVRecord_13_4Test {

    @Test
    @Timeout(8000)
    void testToList() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "comment";
        long recordNumber = 1L;

        // Create CSVRecord instance via constructor
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // Use reflection to access private method toList
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(record);

        // Assert that the returned list matches the values array as list
        assertNotNull(result);
        assertEquals(Arrays.asList(values), result);
        assertEquals(values.length, result.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], result.get(i));
        }
    }

    @Test
    @Timeout(8000)
    void testToListEmptyValues() throws Exception {
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