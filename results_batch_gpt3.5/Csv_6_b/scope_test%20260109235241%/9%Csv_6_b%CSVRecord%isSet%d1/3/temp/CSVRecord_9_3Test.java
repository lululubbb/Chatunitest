package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_3Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[] {"value0", "value1", "value2"};
        mapping = new HashMap<>();
        mapping.put("key0", 0);
        mapping.put("key1", 1);
        mapping.put("key2", 2);

        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NameMappedAndIndexInRange_ReturnsTrue() {
        assertTrue(csvRecord.isSet("key0"));
        assertTrue(csvRecord.isSet("key1"));
        assertTrue(csvRecord.isSet("key2"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NameMappedButIndexOutOfRange_ReturnsFalse() throws Exception {
        mapping.put("keyOutOfRange", values.length);
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);

        assertFalse(csvRecord.isSet("keyOutOfRange"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NameNotMapped_ReturnsFalse() {
        assertFalse(csvRecord.isSet("nonexistentKey"));
        assertFalse(csvRecord.isSet(null));
        assertFalse(csvRecord.isSet(""));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyValuesArray_ReturnsFalse() throws Exception {
        String[] emptyValues = new String[0];
        Map<String, Integer> mapWithZeroIndex = new HashMap<>();
        mapWithZeroIndex.put("key0", 0);
        csvRecord = createCSVRecord(emptyValues, mapWithZeroIndex, "comment", 1L);

        assertFalse(csvRecord.isSet("key0"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_EmptyMapping_ReturnsFalse() throws Exception {
        Map<String, Integer> emptyMapping = Collections.emptyMap();
        csvRecord = createCSVRecord(values, emptyMapping, "comment", 1L);

        assertFalse(csvRecord.isSet("key0"));
    }
}