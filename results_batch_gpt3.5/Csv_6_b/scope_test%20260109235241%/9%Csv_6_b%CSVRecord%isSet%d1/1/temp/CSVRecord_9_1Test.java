package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        values = new String[] {"value1", "value2", "value3"};
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NameMappedAndIndexInBounds_ReturnsTrue() {
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
        assertTrue(csvRecord.isSet("col3"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NameMappedButIndexOutOfBounds_ReturnsFalse() throws Exception {
        // Use reflection to modify the private final mapping field to add "col4" -> 3
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> internalMapping = (Map<String, Integer>) mappingField.get(csvRecord);

        // Replace the internal mapping with a new modifiable map containing the old entries plus the new one
        Map<String, Integer> newMapping = new HashMap<>(internalMapping);
        newMapping.put("col4", 3);

        // Set the new map back to the final field
        mappingField.set(csvRecord, newMapping);

        assertFalse(csvRecord.isSet("col4"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NameNotMapped_ReturnsFalse() {
        assertFalse(csvRecord.isSet("nonexistent"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_PrivateMethodUsingReflection() throws Exception {
        Method isSetMethod = CSVRecord.class.getDeclaredMethod("isSet", String.class);
        isSetMethod.setAccessible(true);

        // Name mapped and index in bounds
        Boolean result1 = (Boolean) isSetMethod.invoke(csvRecord, "col1");
        assertTrue(result1);

        // Modify the private final mapping field to add "col5" -> 10
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> internalMapping = (Map<String, Integer>) mappingField.get(csvRecord);

        // Replace the internal mapping with a new modifiable map containing the old entries plus the new one
        Map<String, Integer> newMapping = new HashMap<>(internalMapping);
        newMapping.put("col5", 10);

        // Set the new map back to the final field
        mappingField.set(csvRecord, newMapping);

        // Name mapped but index out of bounds
        Boolean result2 = (Boolean) isSetMethod.invoke(csvRecord, "col5");
        assertFalse(result2);

        // Name not mapped
        Boolean result3 = (Boolean) isSetMethod.invoke(csvRecord, "unknown");
        assertFalse(result3);
    }
}