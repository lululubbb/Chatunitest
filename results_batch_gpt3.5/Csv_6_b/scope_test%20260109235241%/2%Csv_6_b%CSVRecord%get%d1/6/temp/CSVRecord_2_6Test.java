package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_2_6Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    enum TestEnum {
        FIELD1, FIELD2, FIELD3
    }

    enum OtherEnum {
        NOT_EXIST
    }

    @BeforeEach
    void setUp() {
        values = new String[] {"value0", "value1", "value2"};
        mapping = new HashMap<>();
        mapping.put("FIELD1", 0);
        mapping.put("FIELD2", 1);
        mapping.put("FIELD3", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_existingField() {
        String result = csvRecord.get(TestEnum.FIELD2);
        assertEquals("value1", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nonExistingField() {
        String result = csvRecord.get(OtherEnum.NOT_EXIST);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    void testGet_private_getStringNameMethod() throws Exception {
        Method getStringMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getStringMethod.setAccessible(true);

        Object result1 = getStringMethod.invoke(csvRecord, "FIELD1");
        assertEquals("value0", result1);

        Object result2 = getStringMethod.invoke(csvRecord, "NON_EXIST");
        assertNull(result2);

        Object result3 = getStringMethod.invoke(csvRecord, new Object[] {null});
        assertNull(result3);
    }
}