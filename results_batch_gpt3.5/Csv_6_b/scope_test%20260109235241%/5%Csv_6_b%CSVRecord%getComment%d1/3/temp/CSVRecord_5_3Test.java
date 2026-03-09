package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_5_3Test {

    private CSVRecord csvRecordWithComment;
    private CSVRecord csvRecordWithoutComment;

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to get the package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        String[] values = new String[]{"val1", "val2"};
        Map<String, Integer> mapping = Collections.emptyMap();

        csvRecordWithComment = constructor.newInstance(values, mapping, "This is a comment", 1L);
        csvRecordWithoutComment = constructor.newInstance(values, mapping, null, 2L);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_WithComment() {
        String comment = csvRecordWithComment.getComment();
        assertEquals("This is a comment", comment);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_WithoutComment() {
        String comment = csvRecordWithoutComment.getComment();
        assertEquals(null, comment);
    }
}