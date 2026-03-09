package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_3Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        values = new String[] {"value0", "value1", "value2"};
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        csvRecord = new CSVRecord(values, mapping, null, 1L);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedAndIndexInRange_ReturnsTrue() {
        assertTrue(csvRecord.isSet("col0"));
        assertTrue(csvRecord.isSet("col1"));
        assertTrue(csvRecord.isSet("col2"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedButIndexOutOfRange_ReturnsFalse() {
        mapping.put("col3", 3);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);
        assertFalse(record.isSet("col3"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NotMapped_ReturnsFalse() {
        assertFalse(csvRecord.isSet("nonexistent"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyMapping_ReturnsFalse() {
        CSVRecord record = new CSVRecord(values, Collections.emptyMap(), null, 1L);
        assertFalse(record.isSet("col0"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NullName_ReturnsFalse() {
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_UsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method isSetMethod = CSVRecord.class.getDeclaredMethod("isSet", String.class);
        isSetMethod.setAccessible(true);

        Boolean resultMappedInRange = (Boolean) isSetMethod.invoke(csvRecord, "col0");

        // Create a mock CSVRecord and stub isMapped to call real method
        CSVRecord mockRecord = mock(CSVRecord.class);

        // Use reflection to set the private 'mapping' and 'values' fields on the mock
        Map<String, Integer> mockMapping = Collections.singletonMap("colX", 5);
        String[] mockValues = new String[3]; // length less than 6 to simulate out of range

        java.lang.reflect.Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        mappingField.set(mockRecord, mockMapping);

        java.lang.reflect.Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        valuesField.set(mockRecord, mockValues);

        // Stub isMapped to call real method to avoid conflict with isSet logic
        when(mockRecord.isMapped(anyString())).thenCallRealMethod();

        // Stub isSet to call real method on mock (which now has fields set)
        when(mockRecord.isSet("colX")).thenCallRealMethod();

        Boolean resultMappedOutOfRange = (Boolean) isSetMethod.invoke(mockRecord, "colX");

        Boolean resultNotMapped = (Boolean) isSetMethod.invoke(csvRecord, "nonexistent");

        assertTrue(resultMappedInRange);
        assertFalse(resultNotMapped);
        assertFalse(resultMappedOutOfRange);
    }
}