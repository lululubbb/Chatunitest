package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_8_5Test {

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_withMappingContainingKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        String[] values = new String[]{"value1"};
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);
        boolean result = (boolean) isMappedMethod.invoke(record, "header1");

        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_withMappingNotContainingKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        String[] values = new String[]{"value1"};
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);
        boolean result = (boolean) isMappedMethod.invoke(record, "header2");

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_withNullMapping() throws Exception {
        String[] values = new String[]{"value1"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);

        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);
        boolean result = (boolean) isMappedMethod.invoke(record, "header1");

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_withNullName() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        String[] values = new String[]{"value1"};
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);
        boolean result = (boolean) isMappedMethod.invoke(record, new Object[]{null});

        assertFalse(result);
    }
}