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

public class CSVRecord_4_5Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord record;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        values = new String[] { "val0", "val1", "val2" };
        record = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testGet_ValidName() {
        assertEquals("val0", record.get("A"));
        assertEquals("val1", record.get("B"));
        assertEquals("val2", record.get("C"));
    }

    @Test
    @Timeout(8000)
    public void testGet_MappingNull_ThrowsIllegalStateException() throws Exception {
        CSVRecord recordWithNullMapping = createCSVRecordWithMapping(null, values);
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> recordWithNullMapping.get("A"));
        assertEquals("No header mapping was specified, the record values can't be accessed by name", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGet_NameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> record.get("D"));
        assertTrue(thrown.getMessage().contains("Mapping for D not found"));
        assertTrue(thrown.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    public void testGet_IndexOutOfBounds_ThrowsIllegalArgumentException() throws Exception {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("X", values.length);
        CSVRecord badRecord = createCSVRecordWithMapping(badMapping, values);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> badRecord.get("X"));
        assertTrue(thrown.getMessage().contains("Index for header 'X' is " + values.length));
        assertTrue(thrown.getMessage().contains("but CSVRecord only has " + values.length + " values!"));
    }

    private CSVRecord createCSVRecordWithMapping(Map<String, Integer> map, String[] vals) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) vals, map, null, 1L);
    }
}