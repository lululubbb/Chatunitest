package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_5_5Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, SecurityException {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return constructor.newInstance((Object) values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentReturnsComment() throws Exception {
        String comment = "This is a comment";
        CSVRecord record = createCSVRecord(new String[] { "a", "b" }, Collections.emptyMap(), comment, 1L);
        assertEquals(comment, record.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetCommentReturnsNullWhenNoComment() throws Exception {
        CSVRecord record = createCSVRecord(new String[] { "a", "b" }, Collections.emptyMap(), null, 1L);
        assertNull(record.getComment());
    }
}