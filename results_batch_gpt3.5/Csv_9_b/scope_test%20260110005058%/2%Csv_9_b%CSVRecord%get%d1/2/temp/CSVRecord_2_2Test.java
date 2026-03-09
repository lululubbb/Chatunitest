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
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_2_2Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;

    enum TestEnum {
        KEY1, KEY2, KEY3
    }

    @BeforeEach
    void setUp() {
        String[] values = new String[] { "value0", "value1", "value2" };
        mapping = new HashMap<>();
        mapping.put("KEY1", 0);
        mapping.put("KEY2", 1);
        mapping.put("KEY3", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_existingKey() {
        String result = csvRecord.get(TestEnum.KEY2);
        assertEquals("value1", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nonExistingKey() {
        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn("UNKNOWN");
        String result = csvRecord.get(mockEnum);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nonExistingKey_fixed() {
        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn("UNKNOWN");
        String result = csvRecord.get(mockEnum);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> {
            csvRecord.get((Enum<?>) null);
        });
    }

    @Test
    @Timeout(8000)
    void testGet_privateMethodInvocation() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", Enum.class);
        getMethod.setAccessible(true);
        String result = (String) getMethod.invoke(csvRecord, TestEnum.KEY1);
        assertEquals("value0", result);
    }
}