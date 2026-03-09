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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class CSVRecord_8_2Test {

    @Test
    @Timeout(8000)
    void testIsMapped_mappingNull() throws Exception {
        CSVRecord record = createCSVRecord(null);
        Method isMapped = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMapped.setAccessible(true);

        boolean result = (boolean) isMapped.invoke(record, "anyName");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingEmpty() throws Exception {
        CSVRecord record = createCSVRecord(Collections.emptyMap());
        Method isMapped = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMapped.setAccessible(true);

        boolean result = (boolean) isMapped.invoke(record, "anyName");
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingContainsKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        CSVRecord record = createCSVRecord(mapping);
        Method isMapped = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMapped.setAccessible(true);

        boolean result = (boolean) isMapped.invoke(record, "header1");
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingDoesNotContainKey() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        CSVRecord record = createCSVRecord(mapping);
        Method isMapped = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMapped.setAccessible(true);

        boolean result = (boolean) isMapped.invoke(record, "header2");
        assertFalse(result);
    }

    private CSVRecord createCSVRecord(Map<String, Integer> mapping) throws Exception {
        String[] values = new String[]{"val1", "val2"};
        String comment = null;
        long recordNumber = 1L;

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, comment, recordNumber);
    }
}