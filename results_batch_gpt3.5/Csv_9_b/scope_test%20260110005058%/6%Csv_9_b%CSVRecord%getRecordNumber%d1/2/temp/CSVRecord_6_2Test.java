package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_6_2Test {

    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() throws Exception {
        String[] values = new String[] {"val1", "val2"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "comment";
        long recordNumber = 123L;

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() throws Exception {
        Field recordNumberField = CSVRecord.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        long expectedRecordNumber = recordNumberField.getLong(csvRecord);

        long actualRecordNumber = csvRecord.getRecordNumber();
        assertEquals(expectedRecordNumber, actualRecordNumber);
    }
}