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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_5Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[] {"val0", "val1", "val2"};
        mapping = new HashMap<>();
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);
        mapping.put("key3", 3); // out of bounds index for values length 3

        // Use constructor via reflection since it is package-private
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance((Object) values, mapping, "comment", 1L);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedAndIndexInBounds() {
        // key0, key1, key2 are mapped and index < values.length
        assertTrue(csvRecord.isSet("key0"));
        assertTrue(csvRecord.isSet("key1"));
        assertTrue(csvRecord.isSet("key2"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedButIndexOutOfBounds() {
        // key3 is mapped but index 3 >= values.length (3), should be false
        assertFalse(csvRecord.isSet("key3"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NotMapped() {
        // keyNotPresent is not mapped, should be false
        assertFalse(csvRecord.isSet("keyNotPresent"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NullName() {
        // Passing null should not throw NPE and return false (isMapped returns false)
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyMapping() throws Exception {
        // Create CSVRecord with empty mapping
        Map<String, Integer> emptyMap = Collections.emptyMap();
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord emptyMappingRecord = constructor.newInstance((Object) values, emptyMap, "comment", 1L);

        assertFalse(emptyMappingRecord.isSet("anyKey"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_PrivateMethodViaReflection() throws Exception {
        // Use reflection to invoke private method isMapped(String)
        Method isMappedMethod = CSVRecord.class.getDeclaredMethod("isMapped", String.class);
        isMappedMethod.setAccessible(true);

        Boolean mappedTrue = (Boolean) isMappedMethod.invoke(csvRecord, "key0");
        Boolean mappedFalse = (Boolean) isMappedMethod.invoke(csvRecord, "unknown");

        assertTrue(mappedTrue);
        assertFalse(mappedFalse);
    }
}