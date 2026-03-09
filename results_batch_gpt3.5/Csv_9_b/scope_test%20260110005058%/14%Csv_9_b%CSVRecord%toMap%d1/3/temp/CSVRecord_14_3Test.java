package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_14_3Test {

    private CSVRecord csvRecord;
    private Map<String, Integer> mapping;
    private String[] values;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        // Prepare mapping and values for tests
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        mapping.put("C", 2);
        values = new String[]{"valA", "valB", "valC"};
        comment = "comment";
        recordNumber = 1L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testToMap_returnsMapWithAllEntries() {
        Map<String, String> map = csvRecord.toMap();
        assertNotNull(map);
        assertEquals(mapping.size(), map.size());
        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            assertTrue(map.containsKey(entry.getKey()));
            assertEquals(values[entry.getValue().intValue()], map.get(entry.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    public void testToMap_emptyValuesAndMapping() {
        CSVRecord emptyRecord = new CSVRecord(new String[0], new HashMap<>(), null, 0L);
        Map<String, String> map = emptyRecord.toMap();
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testPutIn_withPartialMapping() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to test putIn method with partial mapping
        Method putInMethod = CSVRecord.class.getDeclaredMethod("putIn", Map.class);
        putInMethod.setAccessible(true);

        Map<String, Integer> partialMapping = new HashMap<>();
        partialMapping.put("A", 0);
        partialMapping.put("C", 2);
        CSVRecord partialRecord = new CSVRecord(values, partialMapping, comment, recordNumber);

        Map<String, String> mapArg = new HashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) putInMethod.invoke(partialRecord, mapArg);

        assertNotNull(result);
        assertEquals(partialMapping.size(), result.size());
        for (Map.Entry<String, Integer> entry : partialMapping.entrySet()) {
            assertTrue(result.containsKey(entry.getKey()));
            assertEquals(values[entry.getValue().intValue()], result.get(entry.getKey()));
        }
    }
}