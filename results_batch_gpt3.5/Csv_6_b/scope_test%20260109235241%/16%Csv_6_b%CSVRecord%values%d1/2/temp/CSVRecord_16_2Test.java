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

public class CSVRecord_16_2Test {

    @Test
    @Timeout(8000)
    void testValues() throws Exception {
        // Prepare test data
        String[] testValues = new String[] {"val1", "val2", "val3"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        String comment = "test comment";
        long recordNumber = 42L;

        // Create instance of CSVRecord using constructor via reflection
        Class<CSVRecord> clazz = CSVRecord.class;
        java.lang.reflect.Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord csvRecord = constructor.newInstance(testValues, mapping, comment, recordNumber);

        // Use reflection to access the package-private values() method
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);

        // Invoke the method and assert the returned array is the same as the field
        String[] returnedValues = (String[]) valuesMethod.invoke(csvRecord);

        assertNotNull(returnedValues);
        assertArrayEquals(testValues, returnedValues);
        // Check that returned array is exactly the same instance as the field
        assertSame(testValues, returnedValues);
    }
}