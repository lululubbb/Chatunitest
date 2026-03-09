package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class CSVRecord_1_3Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[] {"val1", "val2", "val3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "This is a comment";
        recordNumber = 42L;
    }

    @Test
    @Timeout(8000)
    void testConstructorNormal() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // verify fields via reflection
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        assertArrayEquals(values, (String[]) valuesField.get(record));

        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        assertEquals(mapping, mappingField.get(record));

        Field commentField = CSVRecord.class.getDeclaredField("comment");
        commentField.setAccessible(true);
        assertEquals(comment, commentField.get(record));

        Field recordNumberField = CSVRecord.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        assertEquals(recordNumber, recordNumberField.getLong(record));
    }

    @Test
    @Timeout(8000)
    void testConstructorNullValues() throws Exception {
        CSVRecord record = new CSVRecord(null, mapping, comment, recordNumber);

        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] valuesArr = (String[]) valuesField.get(record);

        Field emptyStringArrayField = CSVRecord.class.getDeclaredField("EMPTY_STRING_ARRAY");
        emptyStringArrayField.setAccessible(true);
        String[] emptyArray = (String[]) emptyStringArrayField.get(null);

        assertSame(emptyArray, valuesArr);
    }

    @Test
    @Timeout(8000)
    void testConstructorNullMapping() throws Exception {
        CSVRecord record = new CSVRecord(values, null, comment, recordNumber);

        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        assertNull(mappingField.get(record));
    }

    @Test
    @Timeout(8000)
    void testConstructorNullComment() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, null, recordNumber);

        Field commentField = CSVRecord.class.getDeclaredField("comment");
        commentField.setAccessible(true);
        assertNull(commentField.get(record));
    }
}