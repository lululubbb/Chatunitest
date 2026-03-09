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
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_12_6Test {

    private CSVRecord csvRecordEmpty;
    private CSVRecord csvRecordNonEmpty;

    @BeforeEach
    public void setUp() throws Exception {
        // Using reflection to access the package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        csvRecordEmpty = constructor.newInstance(new Object[] { new String[0], null, null, 0L });

        String[] values = new String[] { "a", "b", "c" };
        csvRecordNonEmpty = constructor.newInstance(new Object[] { values, null, null, 1L });
    }

    @Test
    @Timeout(8000)
    public void testSizeEmpty() {
        assertEquals(0, csvRecordEmpty.size());
    }

    @Test
    @Timeout(8000)
    public void testSizeNonEmpty() {
        assertEquals(3, csvRecordNonEmpty.size());
    }
}