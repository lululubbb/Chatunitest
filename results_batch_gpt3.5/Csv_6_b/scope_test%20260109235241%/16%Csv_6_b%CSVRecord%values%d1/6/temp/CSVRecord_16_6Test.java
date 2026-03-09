package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_16_6Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[] {"value1", "value2", "value3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "a comment";
        recordNumber = 123L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testValuesMethodDirect() throws Exception {
        // Use reflection to access the package-private values() method
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        String[] returnedValues = (String[]) valuesMethod.invoke(csvRecord);

        assertNotNull(returnedValues, "Returned array should not be null");
        assertArrayEquals(values, returnedValues, "Returned array should match the original values");
    }

    @Test
    @Timeout(8000)
    void testValuesMethodEmptyArray() throws Exception {
        // Create CSVRecord with empty values array
        String[] emptyValues = new String[0];
        CSVRecord emptyRecord = new CSVRecord(emptyValues, new HashMap<>(), null, 0L);

        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        String[] returnedValues = (String[]) valuesMethod.invoke(emptyRecord);

        assertNotNull(returnedValues, "Returned array should not be null");
        assertEquals(0, returnedValues.length, "Returned array should be empty");
    }

    @Test
    @Timeout(8000)
    void testValuesMethodNullValuesField() throws Exception {
        // Create CSVRecord with values field forcibly set to null via reflection
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        valuesField.set(record, null);

        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        String[] returnedValues = (String[]) valuesMethod.invoke(record);

        assertNull(returnedValues, "Returned array should be null if values field is null");
    }
}