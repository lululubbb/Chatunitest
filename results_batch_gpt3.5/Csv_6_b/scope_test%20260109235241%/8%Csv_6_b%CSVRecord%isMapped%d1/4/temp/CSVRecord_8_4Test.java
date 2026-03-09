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

class CSVRecord_8_4Test {

    private CSVRecord csvRecordWithMapping;
    private CSVRecord csvRecordWithoutMapping;
    private Map<String, Integer> mapping;

    @BeforeEach
    void setUp() throws Exception {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        String[] values = new String[] {"value1", "value2"};

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        csvRecordWithMapping = constructor.newInstance(values, mapping, "comment", 1L);
        csvRecordWithoutMapping = constructor.newInstance(values, null, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_namePresent() {
        assertTrue(csvRecordWithMapping.isMapped("header1"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_nameAbsent() {
        assertFalse(csvRecordWithMapping.isMapped("nonexistent"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingNull() {
        assertFalse(csvRecordWithoutMapping.isMapped("header1"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_privateMethodReflection() throws Exception {
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        boolean resultPresent = (boolean) isMappedMethod.invoke(csvRecordWithMapping, "header2");
        boolean resultAbsent = (boolean) isMappedMethod.invoke(csvRecordWithMapping, "absent");

        assertTrue(resultPresent);
        assertFalse(resultAbsent);

        boolean resultNullMapping = (boolean) isMappedMethod.invoke(csvRecordWithoutMapping, "header1");
        assertFalse(resultNullMapping);
    }
}