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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_5_3Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, SecurityException {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_NonNullComment() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "This is a comment";
        long recordNumber = 1L;

        CSVRecord record = constructor.newInstance((Object) values, mapping, comment, recordNumber);

        String result = record.getComment();

        assertEquals(comment, result);
    }

    @Test
    @Timeout(8000)
    public void testGetComment_NullComment() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = Collections.emptyMap();
        String comment = null;
        long recordNumber = 2L;

        CSVRecord record = constructor.newInstance((Object) values, mapping, comment, recordNumber);

        String result = record.getComment();

        assertEquals(comment, result);
    }
}