package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CSVRecord_3_1Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    private enum TestEnum { COL0, COL1, COL2 }

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[] { "value0", "value1", "value2" };
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        comment = "comment text";
        recordNumber = 123L;

        // Use reflection to invoke the package-private constructor
        Class<CSVRecord> clazz = CSVRecord.class;
        Constructor<CSVRecord> constructor = clazz.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_ValidIndex() {
        assertEquals("value0", csvRecord.get(0));
        assertEquals("value1", csvRecord.get(1));
        assertEquals("value2", csvRecord.get(2));
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
        assertEquals("value0", csvRecord.get(TestEnum.COL0));
        assertEquals("value1", csvRecord.get(TestEnum.COL1));
        assertEquals("value2", csvRecord.get(TestEnum.COL2));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_NullEnum() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGetByString() {
        assertEquals("value0", csvRecord.get("col0"));
        assertEquals("value1", csvRecord.get("col1"));
        assertEquals("value2", csvRecord.get("col2"));
    }

    @Test
    @Timeout(8000)
    public void testGetByString_NullName() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((String) null));
    }

    @Test
    @Timeout(8000)
    public void testGetByString_UnknownName() {
        assertThrows(IllegalArgumentException.class, () -> csvRecord.get("unknown"));
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
    public void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMapped() {
        assertTrue(csvRecord.isMapped("col0"));
        assertFalse(csvRecord.isMapped("unknown"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet() {
        assertTrue(csvRecord.isSet("col0"));
        assertFalse(csvRecord.isSet("unknown"));
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
    public void testSize() {
        assertEquals(values.length, csvRecord.size());
    }

    @Test
    @Timeout(8000)
    public void testToString_NotEmpty() {
        String str = csvRecord.toString();
        assertNotNull(str);
        assertTrue(str.contains("value0"));
        assertTrue(str.contains("value1"));
        assertTrue(str.contains("value2"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateToList() throws Exception {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<String> list = (java.util.List<String>) toListMethod.invoke(csvRecord);
        assertEquals(values.length, list.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], list.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testValuesArray() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(csvRecord);
        assertArrayEquals(values, vals);
    }

    @Test
    @Timeout(8000)
    public void testPutIn_Map() {
        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(targetMap);
        assertSame(targetMap, result);
        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(values[entry.getValue()], result.get(entry.getKey()));
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
}