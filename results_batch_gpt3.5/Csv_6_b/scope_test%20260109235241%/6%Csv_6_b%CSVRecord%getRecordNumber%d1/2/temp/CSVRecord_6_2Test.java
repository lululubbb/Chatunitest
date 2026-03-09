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

public class CSVRecord_6_2Test {

    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() throws Exception {
        String[] values = new String[]{"value1", "value2"};
        // Using reflection to access the package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        // The mapping parameter should be Map<String, Integer>
        Map<String, Integer> mapping = Collections.emptyMap();
        csvRecord = constructor.newInstance(values, mapping, null, 123L);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() {
        long recordNumber = csvRecord.getRecordNumber();
        assertEquals(123L, recordNumber);
    }
}