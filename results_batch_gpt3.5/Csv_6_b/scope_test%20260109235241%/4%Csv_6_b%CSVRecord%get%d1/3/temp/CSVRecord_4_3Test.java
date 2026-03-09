package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_4_3Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() {
        values = new String[] { "val0", "val1", "val2" };
        mapping = new HashMap<>();
        mapping.put("header0", 0);
        mapping.put("header1", 1);
        mapping.put("header2", 2);
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testGet_WithValidName_ReturnsValue() {
        assertEquals("val0", csvRecord.get("header0"));
        assertEquals("val1", csvRecord.get("header1"));
        assertEquals("val2", csvRecord.get("header2"));
    }

    @Test
    @Timeout(8000)
    void testGet_WithNullMapping_ThrowsIllegalStateException() throws Exception {
        // Use reflection to access package-private constructor with null mapping
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord recordWithNullMapping = constructor.newInstance(values, null, null, 1L);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> recordWithNullMapping.get("header0"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGet_WithNameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> csvRecord.get("missingHeader"));
        assertTrue(exception.getMessage().contains("Mapping for missingHeader not found"));
        assertTrue(exception.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    void testGet_WithIndexOutOfBounds_ThrowsIllegalArgumentException() {
        mapping.put("badIndex", 5);
        // Recreate csvRecord to include the updated mapping
        csvRecord = new CSVRecord(values, mapping, null, 1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> csvRecord.get("badIndex"));
        assertTrue(exception.getMessage().contains("Index for header 'badIndex' is 5 but CSVRecord only has 3 values!"));
    }

    @Test
    @Timeout(8000)
    void testGet_InvokedViaReflection() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);
        Object result = getMethod.invoke(csvRecord, "header1");
        assertEquals("val1", result);
    }
}