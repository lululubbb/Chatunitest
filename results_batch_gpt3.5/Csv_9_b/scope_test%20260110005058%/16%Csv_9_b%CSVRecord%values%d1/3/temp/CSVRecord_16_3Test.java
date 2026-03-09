package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class CSVRecord_16_3Test {

    @Test
    @Timeout(8000)
    void testValues() throws Exception {
        // Prepare test data
        String[] testValues = new String[] { "value1", "value2", "value3" };
        Map<String, Integer> mapping = new HashMap<>();
        String comment = "comment";
        long recordNumber = 1L;

        // Use reflection to get the CSVRecord constructor
        Class<?> clazz = CSVRecord.class;
        java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        // Create CSVRecord instance using constructor
        CSVRecord record = (CSVRecord) constructor.newInstance((Object) testValues, mapping, comment, recordNumber);

        // Use reflection to access the package-private values() method
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        // Invoke values() method
        String[] result = (String[]) valuesMethod.invoke(record);

        // Assert that the returned array is exactly the same as the one passed to constructor
        assertArrayEquals(testValues, result);
    }
}