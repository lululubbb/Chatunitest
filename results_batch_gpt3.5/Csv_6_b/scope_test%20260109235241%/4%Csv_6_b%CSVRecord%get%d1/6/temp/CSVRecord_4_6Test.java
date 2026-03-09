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

class CSVRecord_4_6Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord csvRecord;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] { "value1", "value2" };
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testGet_WithValidName_ReturnsValue() {
        assertEquals("value1", csvRecord.get("header1"));
        assertEquals("value2", csvRecord.get("header2"));
    }

    @Test
    @Timeout(8000)
    void testGet_WithNullMapping_ThrowsIllegalStateException() {
        // Use reflection to invoke the package-private constructor
        CSVRecord record = new CSVRecord(values, null, null, 1L);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> record.get("header1"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGet_WithNameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> csvRecord.get("unknown"));
        assertTrue(exception.getMessage().contains("Mapping for unknown not found"));
        assertTrue(exception.getMessage().contains("expected one of"));
    }

    @Test
    @Timeout(8000)
    void testGet_WithIndexOutOfBounds_ThrowsIllegalArgumentException() {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("header1", 5);
        CSVRecord record = new CSVRecord(values, badMapping, null, 1L);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> record.get("header1"));
        assertTrue(exception.getMessage().contains("Index for header 'header1' is 5 but CSVRecord only has 2 values!"));
    }

    @Test
    @Timeout(8000)
    void testPrivateMethodGetUsingReflection() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);
        Object result = getMethod.invoke(csvRecord, "header1");
        assertEquals("value1", result);
    }
}