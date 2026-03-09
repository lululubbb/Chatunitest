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

public class CSVRecord_8_6Test {

    private CSVRecord csvRecordWithMapping;
    private CSVRecord csvRecordWithoutMapping;
    private Map<String, Integer> mapping;

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> ctor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        ctor.setAccessible(true);

        csvRecordWithMapping = ctor.newInstance(new Object[]{new String[]{"value1", "value2"}, mapping, "comment", 1L});
        csvRecordWithoutMapping = ctor.newInstance(new Object[]{new String[]{"value1", "value2"}, null, "comment", 2L});
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingContainingName() {
        assertTrue(csvRecordWithMapping.isMapped("header1"));
        assertTrue(csvRecordWithMapping.isMapped("header2"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withMappingNotContainingName() {
        assertFalse(csvRecordWithMapping.isMapped("header3"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withNullMapping() {
        assertFalse(csvRecordWithoutMapping.isMapped("header1"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_withNullName() {
        assertFalse(csvRecordWithMapping.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_reflectionInvocation() throws Exception {
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean result1 = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "header1");
        assertTrue(result1);

        Boolean result2 = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, "nonexistent");
        assertFalse(result2);

        Boolean result3 = (Boolean) isMappedMethod.invoke(csvRecordWithoutMapping, "header1");
        assertFalse(result3);

        Boolean result4 = (Boolean) isMappedMethod.invoke(csvRecordWithMapping, (Object) null);
        assertFalse(result4);
    }
}