package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_4_2Test {

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
    public void testGet_ValidName_ReturnsValue() {
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testGet_MappingNull_ThrowsIllegalStateException() throws Exception {
        // Use reflection to create CSVRecord with null mapping
        String[] vals = {"v1"};
        CSVRecord recWithNullMapping = createCSVRecordWithMapping(vals, null);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> recWithNullMapping.get("header1"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGet_NameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> record.get("nonexistent"));
        String expectedMessageStart = "Mapping for nonexistent not found, expected one of ";
        assertTrue(ex.getMessage().startsWith(expectedMessageStart));
        assertTrue(ex.getMessage().contains("header1"));
        assertTrue(ex.getMessage().contains("header2"));
    }

    @Test
    @Timeout(8000)
    public void testGet_IndexOutOfBounds_ThrowsIllegalArgumentException() throws Exception {
        // Create mapping with index out of bounds
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("header1", 10);
        CSVRecord badRecord = createCSVRecordWithMapping(values, badMapping);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> badRecord.get("header1"));
        String expectedMessage = String.format(
                "Index for header '%s' is %d but CSVRecord only has %d values!", "header1", 10, values.length);
        assertEquals(expectedMessage, ex.getMessage());
    }

    // Helper method to create CSVRecord instance with given values and mapping using reflection
    private CSVRecord createCSVRecordWithMapping(String[] values, Map<String, Integer> mapping) throws Exception {
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, null, 1L);
    }
}