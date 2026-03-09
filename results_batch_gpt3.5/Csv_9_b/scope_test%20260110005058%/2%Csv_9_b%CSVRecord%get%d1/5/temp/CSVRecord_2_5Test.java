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

class CSVRecord_2_5Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    private enum TestEnum {
        KEY1, KEY2, KEY3
    }

    private enum OtherEnum {
        UNKNOWN
    }

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("KEY1", 0);
        mapping.put("KEY2", 1);
        mapping.put("KEY3", 2);

        values = new String[] { "value1", "value2", "value3" };

        csvRecord = new CSVRecord(values, mapping, "comment", 123L);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_existingKey() {
        String result = csvRecord.get(TestEnum.KEY1);
        assertEquals("value1", result);

        result = csvRecord.get(TestEnum.KEY2);
        assertEquals("value2", result);

        result = csvRecord.get(TestEnum.KEY3);
        assertEquals("value3", result);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_nonExistingKey() {
        String result = csvRecord.get(OtherEnum.UNKNOWN);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_enum_nullEnum() throws Throwable {
        Method getEnumMethod = CSVRecord.class.getDeclaredMethod("get", Enum.class);
        getEnumMethod.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            getEnumMethod.invoke(csvRecord, new Object[] { null });
        });
        // unwrap the cause which should be NullPointerException
        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof NullPointerException);
        throw cause;
    }

    @Test
    @Timeout(8000)
    void testGet_enum_privateMethodInvocation() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", Enum.class);
        getMethod.setAccessible(true);

        String result = (String) getMethod.invoke(csvRecord, TestEnum.KEY1);
        assertEquals("value1", result);
    }

}