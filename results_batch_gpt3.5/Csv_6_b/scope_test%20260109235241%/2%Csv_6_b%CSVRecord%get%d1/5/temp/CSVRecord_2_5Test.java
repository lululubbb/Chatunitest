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

    enum TestEnum {
        FIELD1, FIELD2, NON_EXISTENT
    }

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("FIELD1", 0);
        mapping.put("FIELD2", 1);
        String[] values = new String[] { "value1", "value2" };
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_existingField() {
        String result = csvRecord.get(TestEnum.FIELD1);
        assertEquals("value1", result);

        result = csvRecord.get(TestEnum.FIELD2);
        assertEquals("value2", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nonExistingField() {
        // The enum toString returns "NON_EXISTENT" which is not in mapping
        String result = csvRecord.get(TestEnum.NON_EXISTENT);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nullEnum() throws Exception {
        // Use reflection to invoke get(Enum<?>) with null to verify NPE or null handling
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", Enum.class);
        getMethod.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            getMethod.invoke(csvRecord, new Object[] { null });
        });
        assertTrue(thrown.getCause() instanceof NullPointerException);
    }
}