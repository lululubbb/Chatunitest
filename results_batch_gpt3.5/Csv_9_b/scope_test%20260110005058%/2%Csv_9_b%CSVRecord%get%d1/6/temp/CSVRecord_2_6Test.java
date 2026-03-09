package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVRecord_2_6Test {

    @Test
    @Timeout(8000)
    void testGet_withEnum_returnsValueForEnumName() throws Exception {
        // Prepare mapping and values
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUMNAME", 1);
        String[] values = new String[] {"zero", "one", "two"};
        String comment = "comment";
        long recordNumber = 5L;

        // Create CSVRecord instance using package-private constructor
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // Create enum mock with name() returning "ENUMNAME"
        Enum<?> enumMock = mock(Enum.class);
        when(enumMock.name()).thenReturn("ENUMNAME");

        // Call get(Enum<?>)
        String result = record.get(enumMock);

        // Assert that it returns values[1] = "one"
        assertEquals("one", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_returnsNullIfMappingAbsent() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        String[] values = new String[] {"zero", "one", "two"};
        String comment = null;
        long recordNumber = 1L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Enum<?> enumMock = mock(Enum.class);
        when(enumMock.name()).thenReturn("UNKNOWN");

        String result = record.get(enumMock);

        assertEquals(null, result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_callsGetStringUsingReflection() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("TEST", 0);
        String[] values = new String[] {"value0"};
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Enum<?> enumMock = mock(Enum.class);
        when(enumMock.name()).thenReturn("TEST");

        // Use reflection to invoke private get(String)
        Method privateGetString = CSVRecord.class.getDeclaredMethod("get", String.class);
        privateGetString.setAccessible(true);

        // Call get(Enum<?>) which calls public get(String), test indirectly
        String resultFromPublic = record.get(enumMock);
        String resultFromPrivate = (String) privateGetString.invoke(record, "TEST");

        assertEquals(resultFromPrivate, resultFromPublic);
    }
}