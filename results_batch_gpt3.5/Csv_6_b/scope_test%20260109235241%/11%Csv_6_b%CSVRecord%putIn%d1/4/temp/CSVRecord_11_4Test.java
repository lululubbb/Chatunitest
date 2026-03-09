package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_11_4Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        mapping = new LinkedHashMap<>();
        values = new String[] {"val0", "val1", "val2"};
    }

    @Test
    @Timeout(8000)
    void testPutIn_withEmptyMapping_returnsEmptyMap() throws Exception {
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> targetMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNonEmptyMapping_populatesMap() throws Exception {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 42L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> targetMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(3, result.size());
        assertEquals("val0", result.get("key0"));
        assertEquals("val1", result.get("key1"));
        assertEquals("val2", result.get("key2"));
    }

    @Test
    @Timeout(8000)
    void testPutIn_withMappingHavingOutOfBoundIndex_throwsArrayIndexOutOfBoundsException() throws Exception {
        mapping.put("key0", 0);
        mapping.put("keyOutOfBounds", 10);
        csvRecord = new CSVRecord(values, mapping, null, 0L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> targetMap = new HashMap<>();

        Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            putInMethod.invoke(csvRecord, targetMap);
        });
        // The cause should be ArrayIndexOutOfBoundsException
        assertTrue(exception.getCause() instanceof ArrayIndexOutOfBoundsException);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNullValuesArray_throwsNullPointerException() throws Exception {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(null, mapping, null, 0L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> targetMap = new HashMap<>();

        Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            putInMethod.invoke(csvRecord, targetMap);
        });
        assertTrue(exception.getCause() instanceof NullPointerException);
    }

    @Test
    @Timeout(8000)
    void testPutIn_withNullMapParameter_throwsNullPointerException() throws Exception {
        mapping.put("key0", 0);
        csvRecord = new CSVRecord(values, mapping, null, 0L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            putInMethod.invoke(csvRecord, new Object[] { null });
        });
        assertTrue(exception.getCause() instanceof NullPointerException);
    }
}