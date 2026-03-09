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

public class CSVRecord_4_6Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord record;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] { "value1", "value2" };
        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testGetWithValidName() {
        assertEquals("value1", record.get("header1"));
        assertEquals("value2", record.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testGetWithNullMapping() throws Exception {
        // Use reflection to create a CSVRecord with null mapping
        CSVRecord recNullMapping = createCSVRecordWithMapping(null);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> recNullMapping.get("header1"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGetWithNameNotInMapping() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> record.get("missing"));
        assertTrue(exception.getMessage().contains("Mapping for missing not found"));
        assertTrue(exception.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    public void testGetWithIndexOutOfBounds() throws Exception {
        // Create a mapping with an index out of bounds of values array
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("badHeader", 5);
        CSVRecord recBadIndex = createCSVRecordWithMappingAndValues(badMapping, new String[] { "v1" });
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> recBadIndex.get("badHeader"));
        assertTrue(exception.getMessage().contains("Index for header 'badHeader' is 5 but CSVRecord only has 1 values!"));
    }

    private CSVRecord createCSVRecordWithMapping(Map<String, Integer> map) throws Exception {
        return createCSVRecordWithMappingAndValues(map, new String[] { "v1", "v2" });
    }

    private CSVRecord createCSVRecordWithMappingAndValues(Map<String, Integer> map, String[] vals) throws Exception {
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) vals, map, null, 1L);
    }
}