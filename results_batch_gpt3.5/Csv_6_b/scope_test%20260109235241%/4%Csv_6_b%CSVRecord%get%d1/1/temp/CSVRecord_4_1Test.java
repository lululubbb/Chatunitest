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

public class CSVRecord_4_1Test {

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
        CSVRecord recWithNullMapping = new CSVRecord(values, null, null, 1L);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> recWithNullMapping.get("header1"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGet_WithNameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> record.get("notExist"));
        assertTrue(thrown.getMessage().contains("Mapping for notExist not found"));
        assertTrue(thrown.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    public void testGet_WithIndexOutOfBounds_ThrowsIllegalArgumentException() {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("header", 5);
        CSVRecord rec = new CSVRecord(values, badMapping, null, 1L);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> rec.get("header"));
        assertTrue(thrown.getMessage().contains("Index for header 'header' is 5 but CSVRecord only has 2 values!"));
    }

    @Test
    @Timeout(8000)
    public void testGet_PrivateMethodAccess_UsingReflection() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);
        Object result = getMethod.invoke(record, "header1");
        assertEquals("value1", result);
    }
}