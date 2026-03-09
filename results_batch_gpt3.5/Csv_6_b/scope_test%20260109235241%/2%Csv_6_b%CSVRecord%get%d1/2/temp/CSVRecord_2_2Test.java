package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_2_2Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord csvRecord;

    enum TestEnum {
        FIELD1, FIELD2, FIELD3
    }

    enum OtherEnum { UNKNOWN }

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("FIELD1", 0);
        mapping.put("FIELD2", 1);
        mapping.put("FIELD3", 2);
        values = new String[] { "value1", "value2", "value3" };
        csvRecord = new CSVRecord(values, mapping, "comment", 5L);
    }

    @Test
    @Timeout(8000)
    void testGetWithEnum_existingField() {
        String result = csvRecord.get(TestEnum.FIELD1);
        assertEquals("value1", result);

        result = csvRecord.get(TestEnum.FIELD2);
        assertEquals("value2", result);

        result = csvRecord.get(TestEnum.FIELD3);
        assertEquals("value3", result);
    }

    @Test
    @Timeout(8000)
    void testGetWithEnum_nonExistingField() {
        String result = csvRecord.get(OtherEnum.UNKNOWN);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetWithEnum_nullEnumToString() {
        TestEnum e = mock(TestEnum.class);
        when(e.toString()).thenReturn(null);
        String result = csvRecord.get(e);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetPrivateMethodInvocation() throws Exception {
        Field field = CSVRecord.class.getDeclaredField("EMPTY_STRING_ARRAY");
        field.setAccessible(true);
        String[] emptyArray = (String[]) field.get(null);
        assertNotNull(emptyArray);
        assertEquals(0, emptyArray.length);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        Object list = toListMethod.invoke(csvRecord);
        assertNotNull(list);
        assertTrue(list instanceof java.util.List);
        assertEquals(values.length, ((java.util.List<?>) list).size());
    }
}