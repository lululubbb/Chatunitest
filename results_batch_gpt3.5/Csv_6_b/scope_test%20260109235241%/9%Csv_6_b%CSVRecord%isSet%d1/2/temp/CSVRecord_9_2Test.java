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

public class CSVRecord_9_2Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        values = new String[]{"value0", "value1", "value2"};
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedAndIndexInBounds() {
        mapping.put("header1", 1);
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
        assertTrue(csvRecord.isSet("header1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedButIndexOutOfBounds() {
        mapping.put("header1", 5);
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
        assertFalse(csvRecord.isSet("header1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NotMapped() {
        // mapping does not contain "header1"
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
        assertFalse(csvRecord.isSet("header1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NullName() {
        // Defensive test: if name is null, isMapped likely returns false
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyValues() {
        mapping.put("header1", 0);
        csvRecord = new CSVRecord(new String[0], mapping, "comment", 1L);
        assertFalse(csvRecord.isSet("header1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_UsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        mapping.put("header1", 1);
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);

        Method isSetMethod = CSVRecord.class.getDeclaredMethod("isSet", String.class);
        isSetMethod.setAccessible(true);

        Object resultTrue = isSetMethod.invoke(csvRecord, "header1");
        assertEquals(Boolean.TRUE, resultTrue);

        Object resultFalse = isSetMethod.invoke(csvRecord, "nonexistent");
        assertEquals(Boolean.FALSE, resultFalse);
    }
}