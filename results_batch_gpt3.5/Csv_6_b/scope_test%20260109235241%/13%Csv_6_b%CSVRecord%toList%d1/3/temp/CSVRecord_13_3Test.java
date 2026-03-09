package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class CSVRecord_13_3Test {

    @Test
    @Timeout(8000)
    void testToList_withValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, "comment", 1L);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(record);

        assertNotNull(result);
        assertEquals(Arrays.asList(values), result);
    }

    @Test
    @Timeout(8000)
    void testToList_withEmptyValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) toListMethod.invoke(record);

        assertNotNull(result);
        assertEquals(Collections.emptyList(), result);
    }
}