package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_5_6Test {

    @Test
    @Timeout(8000)
    void testGetCommentReturnsCorrectComment() throws Exception {
        String[] values = new String[] {"value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "This is a comment";
        long recordNumber = 123L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());
    }

    @Test
    @Timeout(8000)
    void testGetCommentReturnsNullWhenCommentIsNull() throws Exception {
        String[] values = new String[] {"value1"};
        Map<String, Integer> mapping = new HashMap<>();
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, null, recordNumber);
        assertNull(record.getComment());
    }

    // Helper method to create CSVRecord instance with reflection
    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance((Object) values, mapping, comment, recordNumber, false);
    }
}