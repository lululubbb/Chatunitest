package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_5_1Test {

    private CSVRecord csvRecordWithComment;
    private CSVRecord csvRecordWithNullComment;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Prepare mapping
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);

        // Use reflection to access the package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        // Instance with a comment
        csvRecordWithComment = constructor.newInstance(new Object[]{new String[]{"value1", "value2"}, mapping, "This is a comment", 1L});
        // Instance with null comment
        csvRecordWithNullComment = constructor.newInstance(new Object[]{new String[]{"value1", "value2"}, mapping, null, 2L});
    }

    @Test
    @Timeout(8000)
    public void testGetComment_ReturnsComment() {
        assertEquals("This is a comment", csvRecordWithComment.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetComment_ReturnsNullWhenNoComment() {
        assertEquals(null, csvRecordWithNullComment.getComment());
    }
}