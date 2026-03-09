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

public class CSVRecord_4_4Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[]{"val0", "val1", "val2"};
        mapping = new HashMap<>();
        mapping.put("header0", 0);
        mapping.put("header1", 1);
        mapping.put("header2", 2);
        csvRecord = createCSVRecordWithMappingAndValues(mapping, values);
    }

    @Test
    @Timeout(8000)
    public void testGet_ValidName() {
        assertEquals("val0", csvRecord.get("header0"));
        assertEquals("val1", csvRecord.get("header1"));
        assertEquals("val2", csvRecord.get("header2"));
    }

    @Test
    @Timeout(8000)
    public void testGet_NullMapping_ThrowsIllegalStateException() throws Exception {
        CSVRecord recordWithNullMapping = createCSVRecordWithMappingAndValues(null, new String[]{"val0", "val1"});

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            recordWithNullMapping.get("header0");
        });
        assertEquals("No header mapping was specified, the record values can't be accessed by name", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGet_NameNotInMapping_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            csvRecord.get("nonexistent");
        });
        assertTrue(ex.getMessage().contains("Mapping for nonexistent not found"));
        assertTrue(ex.getMessage().contains(mapping.keySet().toString()));
    }

    @Test
    @Timeout(8000)
    public void testGet_IndexOutOfBounds_ThrowsIllegalArgumentException() throws Exception {
        Map<String, Integer> badMapping = new HashMap<>();
        badMapping.put("badHeader", 10);
        CSVRecord recordWithBadIndex = createCSVRecordWithMappingAndValues(badMapping, new String[]{"a", "b"});

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            recordWithBadIndex.get("badHeader");
        });
        assertTrue(ex.getMessage().contains("Index for header 'badHeader' is 10 but CSVRecord only has 2 values!"));
    }

    private CSVRecord createCSVRecordWithMapping(Map<String, Integer> mapping) throws Exception {
        return createCSVRecordWithMappingAndValues(mapping, new String[]{"val0", "val1"});
    }

    private CSVRecord createCSVRecordWithMappingAndValues(Map<String, Integer> mapping, String[] values) throws Exception {
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, null, 1L);
    }
}