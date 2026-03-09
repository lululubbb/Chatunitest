package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_9_1Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare mapping and values for constructor
        mapping = new HashMap<>();
        values = new String[] {"value1", "value2"};

        // Use reflection to invoke the package-private constructor
        csvRecord = createCSVRecord(values, mapping, "comment", 1L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        // CSVRecord constructor is package-private, so use reflection to create instance
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedAndIndexInRange_ReturnsTrue() {
        Map<String, Integer> localMapping = new HashMap<>();
        localMapping.put("name1", 1);
        CSVRecord localRecord = createCSVRecordWithMappingAndValues(localMapping, new String[] {"v0", "v1", "v2"});

        assertTrue(localRecord.isSet("name1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappedButIndexOutOfRange_ReturnsFalse() {
        Map<String, Integer> localMapping = new HashMap<>();
        localMapping.put("name1", 3);
        CSVRecord localRecord = createCSVRecordWithMappingAndValues(localMapping, new String[] {"v0", "v1"});

        assertFalse(localRecord.isSet("name1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_NotMapped_ReturnsFalse() {
        Map<String, Integer> localMapping = new HashMap<>();
        localMapping.put("name1", 1);
        CSVRecord localRecord = createCSVRecordWithMappingAndValues(localMapping, new String[] {"v0", "v1"});

        assertFalse(localRecord.isSet("unknown"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_MappingNullValueIndex_ThrowsNullPointerException() {
        Map<String, Integer> localMapping = new HashMap<>();
        localMapping.put("name1", null);
        CSVRecord localRecord = createCSVRecordWithMappingAndValues(localMapping, new String[] {"v0", "v1"});

        assertThrows(NullPointerException.class, () -> localRecord.isSet("name1"));
    }

    private CSVRecord createCSVRecordWithMappingAndValues(Map<String, Integer> mapping, String[] values) {
        try {
            return createCSVRecord(values, mapping, "comment", 1L);
        } catch (Exception e) {
            fail("Failed to create CSVRecord instance: " + e.getMessage());
            return null;
        }
    }
}