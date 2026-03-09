package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_2_1Test {

    private Map<String, Integer> mapping;
    private String[] values;
    private CSVRecord record;

    enum TestEnum {
        FIELD1, FIELD2, FIELD3
    }

    enum OtherEnum {
        UNKNOWN
    }

    @BeforeEach
    public void setUp() throws Exception {
        mapping = new HashMap<>();
        mapping.put("FIELD1", 0);
        mapping.put("FIELD2", 1);
        mapping.put("FIELD3", 2);
        values = new String[] { "value1", "value2", "value3" };

        // Use reflection to invoke package-private constructor
        record = createCSVRecord(values, mapping, "comment", 123L);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGet_withEnum_existingField() {
        String result = record.get(TestEnum.FIELD1);
        assertEquals("value1", result);
    }

    @Test
    @Timeout(8000)
    public void testGet_withEnum_nonExistingField() {
        String result = record.get(OtherEnum.UNKNOWN);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testGet_withEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGet_callsGetString() throws Exception {
        // Spy on CSVRecord instance
        CSVRecord spyRecord = spy(record);

        // Stub the get(String) method on the spy to return "spyValue" when called with "FIELD2"
        doReturn("spyValue").when(spyRecord).get("FIELD2");

        // Call get(Enum) and verify delegation to get(String)
        String result = spyRecord.get(TestEnum.FIELD2);

        assertEquals("spyValue", result);
        verify(spyRecord).get("FIELD2");
    }
}