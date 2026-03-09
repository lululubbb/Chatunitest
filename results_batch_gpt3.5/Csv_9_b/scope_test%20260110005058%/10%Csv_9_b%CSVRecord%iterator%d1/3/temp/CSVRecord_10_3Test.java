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

import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_10_3Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private String comment;
    private long recordNumber;
    private CSVRecord csvRecord;

    @BeforeEach
    void setUp() {
        mapping = new HashMap<>();
        mapping.put("A", 0);
        mapping.put("B", 1);
        values = new String[] { "value1", "value2" };
        comment = "test comment";
        recordNumber = 123L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testIterator_returnsIteratorOverValues() {
        Iterator<String> iterator = csvRecord.iterator();
        assertNotNull(iterator);
        List<String> iterated = new ArrayList<>();
        iterator.forEachRemaining(iterated::add);
        assertEquals(Arrays.asList(values), iterated);
    }

    @Test
    @Timeout(8000)
    void testIterator_emptyValues() throws Exception {
        // Create CSVRecord with empty values array
        CSVRecord emptyRecord = new CSVRecord(new String[0], Collections.emptyMap(), null, 0L);
        Iterator<String> iterator = emptyRecord.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    void testToListViaReflection() throws Exception {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(csvRecord);
        assertNotNull(list);
        assertEquals(Arrays.asList(values), list);
    }
}