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

public class CSVRecord_12_5Test {

    private CSVRecord csvRecordWithValues;
    private CSVRecord csvRecordEmpty;

    @BeforeEach
    public void setUp() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("header1", 0);
        mapping.put("header2", 1);
        String[] values = new String[] { "value1", "value2" };
        csvRecordWithValues = createCSVRecord(values, mapping, "comment", 1L);
        csvRecordEmpty = createCSVRecord(new String[0], Collections.emptyMap(), null, 0L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber)
            throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class,
                long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testSizeWithValues() {
        assertEquals(2, csvRecordWithValues.size());
    }

    @Test
    @Timeout(8000)
    public void testSizeWithEmptyValues() {
        assertEquals(0, csvRecordEmpty.size());
    }
}