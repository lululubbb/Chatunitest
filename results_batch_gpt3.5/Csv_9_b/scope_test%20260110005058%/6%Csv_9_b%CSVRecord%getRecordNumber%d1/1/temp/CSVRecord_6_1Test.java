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
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_6_1Test {

    private CSVRecord csvRecord;
    private long expectedRecordNumber = 123L;

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to access the package-private constructor
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(new Object[] {new String[0], Collections.emptyMap(), null, expectedRecordNumber});
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() {
        assertEquals(expectedRecordNumber, csvRecord.getRecordNumber());
    }
}