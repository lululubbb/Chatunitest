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

class CSVRecord_2_4Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    enum TestEnum {
        VALUE1, VALUE2
    }

    enum OtherEnum {
        OTHER
    }

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("VALUE1", 0);
        mapping.put("VALUE2", 1);
        values = new String[] { "first", "second" };
        csvRecord = new CSVRecord(values, mapping, "comment", 10L);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_existing() {
        // VALUE1 maps to index 0, should return "first"
        String result = csvRecord.get(TestEnum.VALUE1);
        assertEquals("first", result);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_nonExisting() {
        // Enum not in mapping, should return null
        String result = csvRecord.get(OtherEnum.OTHER);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_nullEnumToString() throws Exception {
        // Create a mock Enum that returns null for toString()
        @SuppressWarnings("unchecked")
        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn(null);

        // The public get(Enum<?>) calls get(null), which should return null
        String result = csvRecord.get(mockEnum);
        assertNull(result);

        // Use reflection to call private get(String) method to verify behavior
        Method getStringMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getStringMethod.setAccessible(true);

        // Also directly invoke private get(String) with null to verify no exception
        String directResult = (String) getStringMethod.invoke(csvRecord, (Object) null);
        assertNull(directResult);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_toStringReturnsEmptyString() {
        @SuppressWarnings("unchecked")
        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn("");
        // "" not in mapping, should return null
        String result = csvRecord.get(mockEnum);
        assertNull(result);
    }
}