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

class CSVRecord_4_5Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord record;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] { "value1", "value2" };
        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    void testGet_ValidName_ReturnsValue() {
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }

    @Test
    @Timeout(8000)
    void testGet_MappingNull_ThrowsIllegalStateException() throws Exception {
        // Use reflection to create CSVRecord with null mapping
        CSVRecord recordWithNullMapping = createCSVRecordWithMapping(null);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> recordWithNullMapping.get("header1"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGet_NameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));
        assertTrue(ex.getMessage().contains("Mapping for unknown not found"));
        assertTrue(ex.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    void testGet_IndexOutOfBounds_ThrowsIllegalArgumentException() throws Exception {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("badHeader", values.length); // index out of bounds
        CSVRecord badRecord = createCSVRecordWithMapping(badMapping);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> badRecord.get("badHeader"));
        assertTrue(ex.getMessage().contains("Index for header 'badHeader' is " + values.length));
        assertTrue(ex.getMessage().contains("CSVRecord only has " + values.length + " values"));
    }

    private CSVRecord createCSVRecordWithMapping(Map<String, Integer> map) throws Exception {
        Constructor<CSVRecord> ctor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);
        return ctor.newInstance((Object) values, map, null, 1L);
    }
}