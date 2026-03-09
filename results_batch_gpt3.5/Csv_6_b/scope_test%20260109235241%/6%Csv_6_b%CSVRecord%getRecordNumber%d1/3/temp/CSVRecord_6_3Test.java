package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_6_3Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setUp() throws Exception {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumberZero() throws Exception {
        String[] values = new String[0];
        Map<String, Integer> mapping = Collections.emptyMap();
        String comment = null;
        long recordNumber = 0L;

        CSVRecord record = constructor.newInstance((Object) values, mapping, comment, recordNumber);
        assertEquals(0L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumberPositive() throws Exception {
        String[] values = new String[]{"a", "b"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        String comment = "comment";
        long recordNumber = 123456789L;

        CSVRecord record = constructor.newInstance((Object) values, mapping, comment, recordNumber);
        assertEquals(123456789L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumberNegative() throws Exception {
        String[] values = new String[]{"x"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "";
        long recordNumber = -1L;

        CSVRecord record = constructor.newInstance((Object) values, mapping, comment, recordNumber);
        assertEquals(-1L, record.getRecordNumber());
    }
}