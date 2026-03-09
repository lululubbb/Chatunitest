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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_10_2Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private String comment;
    private long recordNumber;
    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() {
        mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        values = new String[] { "value1", "value2" };
        comment = "comment";
        recordNumber = 1L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
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
        CSVRecord emptyRecord = new CSVRecord(new String[0], Collections.emptyMap(), null, 0L);
        Iterator<String> iterator = emptyRecord.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testToList_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecord);
        assertEquals(Arrays.asList(values), list);
    }
}