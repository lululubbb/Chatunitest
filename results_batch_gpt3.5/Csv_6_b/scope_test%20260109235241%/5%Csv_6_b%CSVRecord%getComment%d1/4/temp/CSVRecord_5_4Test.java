package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_5_4Test {

    private CSVRecord csvRecordWithComment;
    private CSVRecord csvRecordWithoutComment;

    @BeforeEach
    public void setUp() throws Exception {
        // Prepare constructor parameters
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);

        // Using reflection to get the package-private constructor with correct parameter types
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        // Instance with comment
        csvRecordWithComment = constructor.newInstance(values, mapping, "This is a comment", 1L);

        // Instance with null comment
        csvRecordWithoutComment = constructor.newInstance(values, mapping, null, 2L);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_ReturnsComment() {
        String comment = csvRecordWithComment.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_ReturnsNullWhenNoComment() {
        String comment = csvRecordWithoutComment.getComment();
        assertEquals(null, comment);
    }
}