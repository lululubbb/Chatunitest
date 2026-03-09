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

public class CSVRecord_16_5Test {

    @Test
    @Timeout(8000)
    void testValuesMethod() throws Exception {
        String[] testValues = new String[] { "a", "b", "c" };
        Map<String, Integer> mapping = new HashMap<>();
        // Use package-private constructor (test in same package)
        CSVRecord record = new CSVRecord(testValues, mapping, "comment", 1L);

        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        String[] returnedValues = (String[]) valuesMethod.invoke(record);

        assertArrayEquals(testValues, returnedValues);
    }
}