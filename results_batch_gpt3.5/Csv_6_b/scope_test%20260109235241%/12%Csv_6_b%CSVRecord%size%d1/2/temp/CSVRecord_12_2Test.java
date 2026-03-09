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

public class CSVRecord_12_2Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, SecurityException {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testSize_withNonEmptyValues() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = constructor.newInstance((Object) values, mapping, null, 1L);

        int size = record.size();

        assertEquals(3, size);
    }

    @Test
    @Timeout(8000)
    public void testSize_withEmptyValues() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String[] values = new String[0];
        Map<String, Integer> mapping = Collections.emptyMap();
        CSVRecord record = constructor.newInstance((Object) values, mapping, null, 1L);

        int size = record.size();

        assertEquals(0, size);
    }

    @Test
    @Timeout(8000)
    public void testSize_withNullValues() throws InstantiationException, IllegalAccessException, IllegalArgumentException {
        String[] values = null;
        Map<String, Integer> mapping = Collections.emptyMap();

        try {
            constructor.newInstance((Object) values, mapping, null, 1L);
        } catch (InvocationTargetException e) {
            // If constructor throws NullPointerException, test passes
            assertEquals(NullPointerException.class, e.getCause().getClass());
            return;
        }

        // If no exception, fail the test
        throw new AssertionError("Expected NullPointerException was not thrown");
    }
}