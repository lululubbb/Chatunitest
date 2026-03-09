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

class CSVRecord_2_3Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    enum TestEnum {
        TEST
    }

    @BeforeEach
    void setUp() {
        values = new String[] { "value0", "value1", "value2", "TEST_value" };
        mapping = new HashMap<>();
        mapping.put("0", 0);
        mapping.put("1", 1);
        mapping.put("2", 2);
        mapping.put("TEST", 3);
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_existing() {
        String result = csvRecord.get(TestEnum.TEST);
        assertEquals("TEST_value", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_enumToStringReturnsNull() throws Exception {
        @SuppressWarnings("unchecked")
        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn(null);

        String result = csvRecord.get(mockEnum);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_privateMethod_invocation() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", Enum.class);
        getMethod.setAccessible(true);

        String result = (String) getMethod.invoke(csvRecord, TestEnum.TEST);
        assertEquals("TEST_value", result);
    }
}