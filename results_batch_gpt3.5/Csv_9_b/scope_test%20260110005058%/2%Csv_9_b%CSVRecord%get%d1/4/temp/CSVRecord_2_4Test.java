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

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord record;

    enum TestEnum {
        FIELD1,
        FIELD2,
        FIELD3
    }

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("FIELD1", 0);
        mapping.put("FIELD2", 1);
        mapping.put("FIELD3", 2);
        values = new String[] { "value1", "value2", "value3" };
        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_existingField() {
        String result = record.get((Enum<?>) TestEnum.FIELD1);
        assertEquals("value1", result);

        result = record.get((Enum<?>) TestEnum.FIELD2);
        assertEquals("value2", result);

        result = record.get((Enum<?>) TestEnum.FIELD3);
        assertEquals("value3", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nonExistingField() {
        // Use a mock Enum instance with toString() returning "UNKNOWN"
        TestEnum unknownEnum = mock(TestEnum.class);
        when(unknownEnum.toString()).thenReturn("UNKNOWN");

        String result = record.get((Enum<?>) unknownEnum);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    void testPrivateMethod_values_usingReflection() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, returnedValues);
    }
}