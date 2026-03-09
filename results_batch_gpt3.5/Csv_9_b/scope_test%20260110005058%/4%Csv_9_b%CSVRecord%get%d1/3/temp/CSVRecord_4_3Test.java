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
        // Use reflection to create CSVRecord with null mapping
        CSVRecord recordWithNullMapping = createCSVRecordWithMapping(null);

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> recordWithNullMapping.get("header0"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGet_WithNameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> csvRecord.get("nonexistent"));
        assertTrue(thrown.getMessage().contains("Mapping for nonexistent not found"));
        assertTrue(thrown.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    void testGet_WithIndexOutOfBounds_ThrowsIllegalArgumentException() throws Exception {
        // Create mapping with index out of bounds
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("badHeader", values.length + 10);
        CSVRecord record = createCSVRecordWithMapping(badMapping);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> record.get("badHeader"));
        assertTrue(thrown.getMessage().contains("Index for header 'badHeader' is"));
        assertTrue(thrown.getMessage().contains("but CSVRecord only has"));
    }

    // Helper method to create CSVRecord with given mapping using reflection
    private CSVRecord createCSVRecordWithMapping(Map<String, Integer> mapping) throws Exception {
        Constructor<CSVRecord> ctor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);
        return ctor.newInstance((Object) values, mapping, null, 1L);
    }
}