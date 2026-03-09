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
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_8_1Test {

    @Test
    @Timeout(8000)
    void testIsMapped_mappingNull_returnsFalse() throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        CSVRecord record = constructor.newInstance(new Object[]{new String[]{"val1", "val2"}, null, "comment", 1L});

        assertFalse(record.isMapped("anyKey"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingDoesNotContainKey_returnsFalse() throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("key1", 0);
        CSVRecord record = constructor.newInstance(new Object[]{new String[]{"value1"}, mapping, null, 2L});

        assertFalse(record.isMapped("absentKey"));
    }

    @Test
    @Timeout(8000)
    void testIsMapped_mappingContainsKey_returnsTrue() throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("existingKey", 0);
        CSVRecord record = constructor.newInstance(new Object[]{new String[]{"value1"}, mapping, null, 3L});

        assertTrue(record.isMapped("existingKey"));
    }
}