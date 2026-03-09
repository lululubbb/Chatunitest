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
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_6_5Test {

    private CSVRecord csvRecord;
    private long recordNumber = 123L;

    @BeforeEach
    public void setUp() throws Exception {
        String[] values = new String[] {"value1", "value2"};
        // mapping is not used by getRecordNumber, so can be null or empty
        csvRecord = createCSVRecord(values, Map.of(), null, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    // Helper method to create CSVRecord instance via reflection
    private CSVRecord createCSVRecord(String[] values,
                                      Map<String, Integer> mapping,
                                      String comment,
                                      long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }
}