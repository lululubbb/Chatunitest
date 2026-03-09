package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

public class CSVRecord_5_6Test {

    @Test
    @Timeout(8000)
    public void testGetComment_NonNullComment() throws Exception {
        String comment = "This is a comment";
        String[] values = new String[] {"val1", "val2"};
        Map<String, Integer> mapping = Collections.emptyMap();
        long recordNumber = 1L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        assertEquals(comment, record.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetComment_NullComment() throws Exception {
        String comment = null;
        String[] values = new String[] {"val1", "val2"};
        Map<String, Integer> mapping = Collections.emptyMap();
        long recordNumber = 2L;

        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);

        assertNull(record.getComment());
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber, false);
    }
}