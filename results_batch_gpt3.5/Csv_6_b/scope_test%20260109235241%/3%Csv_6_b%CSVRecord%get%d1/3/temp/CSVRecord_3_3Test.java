package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_3_3Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() throws Exception {
        values = new String[] { "value0", "value1", "value2" };
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        comment = "comment";
        recordNumber = 42L;
        csvRecord = createCSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_ValidIndices() {
        for (int i = 0; i < values.length; i++) {
            String result = csvRecord.get(i);
            assertEquals(values[i], result);
        }
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_IndexOutOfBounds_Negative() {
        assertThrows(IndexOutOfBoundsException.class, () -> csvRecord.get(-1));
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_IndexOutOfBounds_TooLarge() {
        assertThrows(IndexOutOfBoundsException.class, () -> csvRecord.get(values.length));
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_EmptyValuesArray() throws Exception {
        // Create CSVRecord with empty values array using reflection because constructor is package-private
        CSVRecord emptyRecord = createCSVRecord(new String[0], Collections.emptyMap(), null, 1L);
        assertThrows(IndexOutOfBoundsException.class, () -> emptyRecord.get(0));
    }

    @Test
    @Timeout(8000)
    void testGetPrivateMethodValuesUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(csvRecord);
        assertArrayEquals(values, returnedValues);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        java.lang.reflect.Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }
}