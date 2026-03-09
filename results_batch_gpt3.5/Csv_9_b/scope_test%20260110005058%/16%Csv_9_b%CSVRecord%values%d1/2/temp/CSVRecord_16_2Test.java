package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVRecord_16_2Test {

    @Test
    @Timeout(8000)
    void testValues_returnsInternalValuesArray() throws Exception {
        // Prepare test data
        String[] expectedValues = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "comment";
        long recordNumber = 1L;

        // Create CSVRecord instance using constructor
        CSVRecord record = new CSVRecord(expectedValues, mapping, comment, recordNumber);

        // Use reflection to invoke package-private values() method
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        String[] actualValues = (String[]) valuesMethod.invoke(record);

        // Assert the returned array is the same as the internal values array
        assertSame(expectedValues, actualValues, "values() should return the internal values array");
        assertArrayEquals(expectedValues, actualValues, "values() content should match the expected values");
    }
}