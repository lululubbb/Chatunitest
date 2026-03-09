package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class CSVRecord_16_4Test {

    @Test
    @Timeout(8000)
    void testValues() throws Exception {
        String[] expectedValues = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "comment";
        long recordNumber = 123L;

        // Create instance of CSVRecord using constructor
        CSVRecord record = new CSVRecord(expectedValues, mapping, comment, recordNumber);

        // Access package-private method values() via reflection
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        String[] actualValues = (String[]) valuesMethod.invoke(record);

        assertArrayEquals(expectedValues, actualValues);
    }
}