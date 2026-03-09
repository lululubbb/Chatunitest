package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_11_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        values = new String[] {"val0", "val1"};
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withEmptyMapping() {
        mapping.clear();
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertTrue(result.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withNonEmptyMapping() {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(2, result.size());
        assertEquals("val0", result.get("key0"));
        assertEquals("val1", result.get("key1"));
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withNullValuesInArray() {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        values = new String[] {null, "val1"};
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = invokePutIn(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(2, result.size());
        assertNull(result.get("key0"));
        assertEquals("val1", result.get("key1"));
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withInvalidIndexInMapping_shouldThrowArrayIndexOutOfBoundsException() {
        mapping.put("key0", 5); // index out of bounds
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();

        ArrayIndexOutOfBoundsException thrown = assertThrows(ArrayIndexOutOfBoundsException.class,
            () -> invokePutIn(csvRecord, targetMap));

        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    public void testPutIn_usingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, String> targetMap = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(csvRecord, targetMap);

        assertSame(targetMap, result);
        assertEquals(2, result.size());
        assertEquals("val0", result.get("key0"));
        assertEquals("val1", result.get("key1"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> invokePutIn(CSVRecord record, Map<String, String> targetMap) {
        try {
            Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
            putInMethod.setAccessible(true);
            return (Map<String, String>) putInMethod.invoke(record, targetMap);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException(e);
        }
    }
}