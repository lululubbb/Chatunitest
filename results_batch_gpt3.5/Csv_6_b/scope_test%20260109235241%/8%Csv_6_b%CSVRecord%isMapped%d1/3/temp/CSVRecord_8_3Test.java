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

public class CSVRecord_8_3Test {

    private CSVRecord csvRecordWithMapping;
    private CSVRecord csvRecordWithoutMapping;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare a mapping with some keys
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        // Create CSVRecord instances via reflection since constructor is package-private
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        String[] values = new String[] {"value1", "value2"};
        csvRecordWithMapping = constructor.newInstance((Object) values, mapping, "comment", 1L);
        csvRecordWithoutMapping = constructor.newInstance((Object) values, null, "comment", 2L);
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingKeyPresent() {
        assertTrue(csvRecordWithMapping.isMapped("header1"));
        assertTrue(csvRecordWithMapping.isMapped("header2"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingKeyAbsent() {
        assertFalse(csvRecordWithMapping.isMapped("nonexistent"));
        assertFalse(csvRecordWithMapping.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withoutMapping() {
        assertFalse(csvRecordWithoutMapping.isMapped("header1"));
        assertFalse(csvRecordWithoutMapping.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_privateMethodInvocation() throws Exception {
        // Use reflection to invoke isMapped method (even though it's public, just to demonstrate)
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean result1 = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "header1");
        Boolean result2 = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "nonexistent");
        Boolean result3 = (Boolean) isMappedMethod.invoke(csvRecordWithoutMapping, "header1");

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
    }
}