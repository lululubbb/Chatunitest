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

public class CSVRecord_8_1Test {

    private CSVRecord csvRecordWithMapping;
    private CSVRecord csvRecordWithoutMapping;
    private Map<String, Integer> mapping;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare mapping with some keys
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        // Create CSVRecord instances using constructor via reflection (package-private)
        csvRecordWithMapping = createCSVRecord(new String[]{"value1", "value2"}, mapping, "comment", 1L);
        csvRecordWithoutMapping = createCSVRecord(new String[]{"value1", "value2"}, null, "comment", 1L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        // Constructor: CSVRecord(final String[] values, final Map<String, Integer> mapping, final String comment, final long recordNumber)
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingContainsKey() throws Exception {
        // Test with mapping != null and mapping contains key
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean result = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "header1");
        assertTrue(result);

        result = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "header2");
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingDoesNotContainKey() throws Exception {
        // Test with mapping != null but does not contain key
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean result = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "unknown");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withNullMapping() throws Exception {
        // Test with mapping == null
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean result = (Boolean) isMappedMethod.invoke(csvRecordWithoutMapping, "header1");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withNullName() throws Exception {
        // Test with null name argument, mapping != null
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean result = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, (Object) null);
        // mapping.containsKey(null) returns false normally, so expect false
        assertFalse(result);

        // Also test with null mapping and null name
        result = (Boolean) isMappedMethod.invoke(csvRecordWithoutMapping, (Object) null);
        assertFalse(result);
    }
}