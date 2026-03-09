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

public class CSVRecord_3_2Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    private enum TestEnum { col0, col1, col2 }

    @BeforeEach
    public void setUp() {
        values = new String[] { "val0", "val1", "val2" };
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        comment = "comment";
        recordNumber = 123L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_ValidIndex() {
        assertEquals("val0", csvRecord.get(0));
        assertEquals("val1", csvRecord.get(1));
        assertEquals("val2", csvRecord.get(2));
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_IndexOutOfBounds_Negative() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(-1));
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_IndexOutOfBounds_TooLarge() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(values.length));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum() {
        assertEquals("val0", csvRecord.get(TestEnum.col0));
        assertEquals("val1", csvRecord.get(TestEnum.col1));
        assertEquals("val2", csvRecord.get(TestEnum.col2));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_NullEnum() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_Existing() {
        assertEquals("val0", csvRecord.get("col0"));
        assertEquals("val1", csvRecord.get("col1"));
        assertEquals("val2", csvRecord.get("col2"));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_NonExisting() {
        assertThrows(IllegalArgumentException.class, () -> csvRecord.get("nonexistent"));
    }

    @Test
    @Timeout(8000)
    public void testGetComment() {
        assertEquals(comment, csvRecord.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_True() {
        // values length equals mapping size
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_False() {
        String[] vals = new String[] { "val0", "val1" };
        CSVRecord record = new CSVRecord(vals, mapping, comment, recordNumber);
        assertFalse(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_True() {
        assertTrue(csvRecord.isMapped("col0"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_False() {
        assertFalse(csvRecord.isMapped("nonexistent"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_True() {
        assertTrue(csvRecord.isSet("col0"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_False() {
        CSVRecord record = new CSVRecord(new String[] { null }, Collections.singletonMap("col0", 0), comment, recordNumber);
        assertFalse(record.isSet("col0"));
    }

    @Test
    @Timeout(8000)
    public void testIterator() {
        int count = 0;
        for (String val : csvRecord) {
            assertEquals(values[count], val);
            count++;
        }
        assertEquals(values.length, count);
    }

    @Test
    @Timeout(8000)
    public void testPutIn() {
        Map<String, String> target = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(target);
        assertSame(target, result);
        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(values[entry.getValue()], target.get(entry.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    public void testSize() {
        assertEquals(values.length, csvRecord.size());
    }

    @Test
    @Timeout(8000)
    public void testToList_Reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method toList = CSVRecord.class.getDeclaredMethod("toList");
        toList.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<String> list = (java.util.List<String>) toList.invoke(csvRecord);
        assertEquals(values.length, list.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], list.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(mapping.size(), map.size());
        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(values[entry.getValue()], map.get(entry.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    public void testToString() {
        String str = csvRecord.toString();
        for (String val : values) {
            assertTrue(str.contains(val));
        }
    }

    @Test
    @Timeout(8000)
    public void testValues_Reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(csvRecord);
        assertArrayEquals(values, vals);
    }
}