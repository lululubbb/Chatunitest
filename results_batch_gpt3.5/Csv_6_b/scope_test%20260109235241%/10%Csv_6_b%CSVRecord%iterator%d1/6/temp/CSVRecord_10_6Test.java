package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_10_6Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "some comment";
        recordNumber = 123L;

        csvRecord = createCSVRecord(values, mapping, comment, recordNumber);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) {
        try {
            Class<CSVRecord> clazz = CSVRecord.class;
            Constructor<CSVRecord> ctor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
            ctor.setAccessible(true);
            return ctor.newInstance(values, mapping, comment, recordNumber);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create CSVRecord instance", e);
        }
    }

    @Test
    @Timeout(8000)
    public void testIterator_returnsIteratorOverValuesList() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);

        List<String> expectedList = Arrays.asList(values);
        List<String> actualList = new ArrayList<>();
        iterator.forEachRemaining(actualList::add);

        assertEquals(expectedList, actualList);
    }

    @Test
    @Timeout(8000)
    public void testIterator_emptyValues() {
        CSVRecord emptyRecord = createCSVRecord(new String[0], Collections.emptyMap(), null, 0L);
        Iterator<String> iterator = emptyRecord.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testToList_privateMethod() throws Exception {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecord);
        assertEquals(Arrays.asList(values), list);
    }
}