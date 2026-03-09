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

class CSVRecord_4_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    void setUp() throws Exception {
        values = new String[] { "val0", "val1", "val2" };
        mapping = new HashMap<>();
        mapping.put("header0", 0);
        mapping.put("header1", 1);
        mapping.put("header2", 2);

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(values, mapping, null, 1L);
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
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord recordWithNullMapping = constructor.newInstance(values, null, null, 1L);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> recordWithNullMapping.get("header0"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGet_WithNameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> csvRecord.get("missing"));
        assertTrue(ex.getMessage().contains("Mapping for missing not found"));
        assertTrue(ex.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    void testGet_WithIndexOutOfBounds_ThrowsIllegalArgumentException() throws Exception {
        mapping.put("badIndex", values.length + 10);
        // Recreate csvRecord to reflect updated mapping
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(values, mapping, null, 1L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> csvRecord.get("badIndex"));
        assertTrue(ex.getMessage().contains("Index for header 'badIndex' is"));
        assertTrue(ex.getMessage().contains("but CSVRecord only has"));
    }

    @Test
    @Timeout(8000)
    void testGet_PrivateMethodInvocation_UsingReflection() throws Exception {
        Method getMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);
        Object result = getMethod.invoke(csvRecord, "header1");
        assertEquals("val1", result);
    }
}