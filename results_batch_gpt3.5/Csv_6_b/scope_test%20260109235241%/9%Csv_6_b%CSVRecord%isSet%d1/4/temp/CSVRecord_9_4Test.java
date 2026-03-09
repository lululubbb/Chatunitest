package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_4Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        values = new String[] {"val0", "val1", "val2"};
        // Use reflection to invoke package-private constructor
        csvRecord = createCSVRecord(values, mapping, null, 1L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        // The constructor is package-private, so use reflection to access it
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameNotMapped_returnsFalse() {
        // mapping is empty, so isMapped(name) returns false
        assertFalse(csvRecord.isSet("anyName"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexLessThanValuesLength_returnsTrue() {
        mapping.put("key1", 1);
        assertTrue(csvRecord.isSet("key1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexEqualsValuesLength_returnsFalse() {
        mapping.put("key1", values.length);
        assertFalse(csvRecord.isSet("key1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_nameMappedIndexGreaterThanValuesLength_returnsFalse() {
        mapping.put("key1", values.length + 1);
        assertFalse(csvRecord.isSet("key1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_withNullName_returnsFalse() {
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_usingReflection() throws Exception {
        // Access private method isMapped via reflection to verify behavior indirectly
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        mapping.put("key1", 0);

        boolean isMappedResult = (boolean) isMappedMethod.invoke(csvRecord, "key1");
        assertTrue(isMappedResult);

        boolean isMappedResultFalse = (boolean) isMappedMethod.invoke(csvRecord, "unknown");
        assertFalse(isMappedResultFalse);
    }
}