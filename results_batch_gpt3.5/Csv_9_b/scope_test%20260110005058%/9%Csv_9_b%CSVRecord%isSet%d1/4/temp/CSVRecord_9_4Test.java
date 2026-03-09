package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_4Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        values = new String[] {"value0", "value1", "value2"};
        // Use reflection to access package-private constructor
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameNotMapped() throws Exception {
        // mapping is empty, so isMapped(name) should be false
        assertFalse(csvRecord.isSet("nonexistent"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexInBounds() throws Exception {
        mapping.put("field1", 1);
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
        assertTrue(csvRecord.isSet("field1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexEqualsLength() throws Exception {
        mapping.put("field2", values.length);
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
        assertFalse(csvRecord.isSet("field2"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexGreaterThanLength() throws Exception {
        mapping.put("field3", values.length + 1);
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
        assertFalse(csvRecord.isSet("field3"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_withNullName() throws Exception {
        CSVRecord spyRecord = spy(csvRecord);
        doReturn(false).when(spyRecord).isMapped((String) null);
        assertFalse(spyRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_withEmptyMappingAndEmptyValues() throws Exception {
        CSVRecord emptyRecord = createCSVRecord(new String[0], Collections.emptyMap(), null, 0L);
        assertFalse(emptyRecord.isSet("any"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_reflectionInvoke() throws Exception {
        Method isSetMethod = CSVRecord.class.getDeclaredMethod("isSet", String.class);
        isSetMethod.setAccessible(true);

        mapping.put("key", 0);
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);

        Object result = isSetMethod.invoke(csvRecord, "key");
        assertEquals(true, result);

        result = isSetMethod.invoke(csvRecord, "nonexistent");
        assertEquals(false, result);

        result = isSetMethod.invoke(csvRecord, (Object) null);
        assertEquals(false, result);
    }
}