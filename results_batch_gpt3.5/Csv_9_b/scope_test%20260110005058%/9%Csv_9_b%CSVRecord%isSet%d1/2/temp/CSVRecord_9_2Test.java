package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_2Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare values array
        values = new String[] { "value0", "value1", "value2" };

        // Prepare mapping map
        mapping = new HashMap<>();
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        mapping.put("key3", 3); // Intentionally out of bounds index

        // Use constructor to create CSVRecord instance
        csvRecord = new CSVRecord(values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedAndIndexInBounds_ReturnsTrue() {
        assertTrue(csvRecord.isSet("key0"));
        assertTrue(csvRecord.isSet("key1"));
        assertTrue(csvRecord.isSet("key2"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedButIndexOutOfBounds_ReturnsFalse() {
        // "key3" maps to index 3, but values length is 3, so index 3 is out of bounds
        assertFalse(csvRecord.isSet("key3"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NotMapped_ReturnsFalse() {
        assertFalse(csvRecord.isSet("nonexistent"));
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyMapping_ReturnsFalse() throws Exception {
        // Create CSVRecord with empty mapping
        Map<String, Integer> emptyMapping = Collections.emptyMap();
        CSVRecord record = new CSVRecord(values, emptyMapping, "comment", 1L);

        assertFalse(record.isSet("key0"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyValues_ReturnsFalse() throws Exception {
        String[] emptyValues = new String[0];
        CSVRecord record = new CSVRecord(emptyValues, mapping, "comment", 1L);

        // All mapped keys should be false because values array is empty
        assertFalse(record.isSet("key0"));
        assertFalse(record.isSet("key1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_PrivateMappingModification() throws Exception {
        // Use reflection to modify private field 'mapping' to null and test NPE safety
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        mappingField.set(csvRecord, null);

        // isSet will throw NullPointerException because mapping is null
        assertThrows(NullPointerException.class, () -> csvRecord.isSet("key0"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_PrivateValuesModification() throws Exception {
        // Use reflection to modify private field 'values' to null and test NPE safety
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        valuesField.set(csvRecord, null);

        // isSet will throw NullPointerException because values is null
        assertThrows(NullPointerException.class, () -> csvRecord.isSet("key0"));
    }
}