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

class CSVRecord_3_6Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[] {"val0", "val1", "val2"};
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        comment = "a comment";
        recordNumber = 42L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testGetByIndexValid() {
        assertEquals("val0", csvRecord.get(0));
        assertEquals("val1", csvRecord.get(1));
        assertEquals("val2", csvRecord.get(2));
    }

    @Test
    @Timeout(8000)
    void testGetByIndexOutOfBoundsNegative() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(-1));
    }

    @Test
    @Timeout(8000)
    void testGetByIndexOutOfBoundsTooLarge() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(values.length));
    }

    @Test
    @Timeout(8000)
    void testGetPrivateValuesFieldViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", int.class);
        getMethod.setAccessible(true);

        assertEquals("val0", getMethod.invoke(csvRecord, 0));
        assertEquals("val1", getMethod.invoke(csvRecord, 1));
        assertEquals("val2", getMethod.invoke(csvRecord, 2));
    }
}