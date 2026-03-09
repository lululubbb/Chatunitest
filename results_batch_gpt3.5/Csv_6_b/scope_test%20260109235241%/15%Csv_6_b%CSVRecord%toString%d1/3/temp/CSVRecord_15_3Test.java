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

import org.junit.jupiter.api.Test;

public class CSVRecord_15_3Test {

    @Test
    @Timeout(8000)
    public void testToString_withValues() throws Exception {
        String[] values = new String[] { "one", "two", "three" };
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "comment";
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        String expected = "[one, two, three]";
        assertEquals(expected, record.toString());
    }

    @Test
    @Timeout(8000)
    public void testToString_withEmptyValues() throws Exception {
        String[] values = new String[0];
        Map<String, Integer> mapping = new HashMap<>();
        String comment = null;
        long recordNumber = 0L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        String expected = "[]";
        assertEquals(expected, record.toString());
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment,
            long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }
}