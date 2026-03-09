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

public class CSVRecord_4_4Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord record;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] {"value1", "value2"};
        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testGet_WithValidName_ReturnsValue() {
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testGet_WithNullMapping_ThrowsIllegalStateException() {
        CSVRecord recordWithNullMapping = new CSVRecord(values, null, null, 1L);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> recordWithNullMapping.get("header1"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGet_WithNameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));
        assertTrue(thrown.getMessage().contains("Mapping for unknown not found"));
        assertTrue(thrown.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    public void testGet_WithIndexOutOfBounds_ThrowsIllegalArgumentException() {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("header1", 10);
        CSVRecord badRecord = new CSVRecord(values, badMapping, null, 1L);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> badRecord.get("header1"));
        assertTrue(thrown.getMessage().contains("Index for header 'header1' is 10 but CSVRecord only has 2 values!"));
    }

    @Test
    @Timeout(8000)
    public void testGet_PrivateMethodInvocation_UsingReflection() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);
        String result = (String) getMethod.invoke(record, "header1");
        assertEquals("value1", result);
    }
}