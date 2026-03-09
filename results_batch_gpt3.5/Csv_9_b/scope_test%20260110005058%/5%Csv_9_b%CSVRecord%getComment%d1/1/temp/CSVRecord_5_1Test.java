package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_5_1Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws Exception {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_NotNull() throws Exception {
        String expectedComment = "This is a comment";
        String[] values = new String[] {"val1", "val2"};
        Map<String, Integer> mapping = new HashMap<>();
        long recordNumber = 1L;

        CSVRecord record = constructor.newInstance(values, mapping, expectedComment, recordNumber);

        String actualComment = record.getComment();

        assertEquals(expectedComment, actualComment);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_Null() throws Exception {
        String expectedComment = null;
        String[] values = new String[] {"val1", "val2"};
        Map<String, Integer> mapping = new HashMap<>();
        long recordNumber = 2L;

        CSVRecord record = constructor.newInstance(values, mapping, expectedComment, recordNumber);

        String actualComment = record.getComment();

        assertNull(actualComment);
    }
}